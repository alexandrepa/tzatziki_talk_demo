Feature: [ORDER BOOKING] We can book items for an order using /orders/<orderId>/create providing the items to book along with their quantity.
  The target warehouse should be extracted from the two first characters of the orderId
  It should then:
  - call /stock-api/<warehouse>/book with the given items
  - fetch price from /price-api/items/{item_id} for every item
  - add a row to booking table with the booked items and the price when the call was done
  - send a 'BOOK' stock movement to <warehouse>_stock_movements for financial purposes
  - return to the caller the booked items along with their price

  Background:
    * a root logger set to INFO
    * this avro schema:
    """
    {{{[&avro/stock_movement.avsc]}}}
    """

  Scenario Template: Nominal case
    Given that posting on "/stock-api/.*/book" will return:
    """
    <itemsToBook>
    """
    Given that getting on "/price-api/items/(.*)" will return:
    """
    $1.99
    """
    When we post on "/orders/<orderId>/create":
    """
    <itemsToBook>
    """
    Then we received a status OK_200 and:
    """
    {{#foreach '<itemsToBook>'}}
    - item_id: '{{this.item_id}}'
      quantity: {{this.quantity}}
      price: {{this.item_id}}.99
    {{/foreach}}
    """
    And "/stock-api/<warehouse>/book" has received a POST and:
    """
    <itemsToBook>
    """
    And the interactions on "/price-api/items/.*" were only:
    """
    {{#foreach '<itemsToBook>'}}
    - request:
        method: GET
        headers.warehouse: <warehouse>
      response.body.payload: {{this.item_id}}.99
    {{/foreach}}
    """
    And the booking table contains exactly:
    """
    id: ?notNull
    order_id: <orderId>
    booked_items_with_price:
    {{#foreach '<itemsToBook>'}}
      - item_id: '{{this.item_id}}'
        quantity: {{this.quantity}}
        price: {{this.item_id}}.99
    {{/foreach}}
    """
    And the <warehouse>_stock_movements topic contains these StockMovements:
    """
    {{#foreach '<itemsToBook>'}}
    - warehouse: <warehouse>
      item_id: '{{this.item_id}}'
      stock_movement_type: BOOK
      price: {{this.item_id}}.99
      quantity: {{this.quantity}}
    {{/foreach}}
    """

    Examples:
      | orderId | itemsToBook                                                        | warehouse |
      | FR1     | [{"item_id": "1", "quantity": 1}, {"item_id": "2", "quantity": 2}] | FR        |
      | IT2     | [{"item_id": "1", "quantity": 2}, {"item_id": "3", "quantity": 1}] | IT        |