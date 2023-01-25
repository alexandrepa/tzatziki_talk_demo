Feature: [ORDER BOOKING] We can book items for an order using /warehouses/{warehouse}/orders providing the items to book along with their quantity.
  The warehouse country should be extracted from the first two warehouse characters
  It should then:
  - call /stock-api/{warehouse}/book with the given items
  - fetch price from /price-api/items/{item_id} for every item for the warehouse country
  - add a row to booking table with the order id, booked items and the price when the call was done
  - send a 'BOOK' stock movement to {warehouse}_stock_movements for financial purposes
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
    When we post on "/warehouses/<warehouse>/orders":
    """
    <itemsToBook>
    """
    Then we received a status OK_200 and:
    """
    order_id: ?notNull
    booked_items_with_price:
    {{#foreach '<itemsToBook>'}}
    - item_id: '{{this.item_id}}'
      quantity: {{this.quantity}}
      price: {{this.item_id}}.99
    {{/foreach}}
    """
    And we log as INFO:
    """
    {{[orderId: _response.body.payload.order_id]}}
    """
    And "/stock-api/<warehouse>/book" has received a POST and:
    """
    <itemsToBook>
    """
    And the interactions on "/price-api/items/.*" were:
    """
    {{#foreach '<itemsToBook>'}}
    - request:
        method: GET
        headers.country: <country>
      response.body.payload: {{this.item_id}}.99
    {{/foreach}}
    """
    And the booking table contains exactly:
    """
    id: ?notNull
    order_id: {{orderId}}
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
      | warehouse | country | itemsToBook                                                        |
      | FR01      | FR      | [{"item_id": "1", "quantity": 1}, {"item_id": "2", "quantity": 2}] |
      | IT01      | IT      | [{"item_id": "1", "quantity": 2}, {"item_id": "3", "quantity": 1}] |