Feature: [ITEM BOOKING] We can book an item for a given order id. We can also check which items have been booked for a given order id.
  These operations are through REST API.

  Scenario Template: We can book an item for a given order id. This should trigger a database insert with order_id, article_id and quantity.
  A message should be sent through KAFKA to inform partners of the stock movement.
    Given a root logger set to INFO
    When we post on "/orders/<orderId>/book":
    """
    {{#foreach [<itemsToBook>]}}
    - item: '{{this.name}}'
      quantity: {{this.quantity}}
    {{/foreach}}
    """
    Then we received a status OK_200 and:
    """
    Booking done
    """
    Then the booking table contains:
    """
    {{#foreach [<itemsToBook>]}}
    - id: ?notNull
      order_id: <orderId>
      item: '{{this.name}}'
      quantity: {{this.quantity}}
    {{/foreach}}
    """
    And the stock_update topic contains these json messages:
    """
    {{#foreach [<itemsToBook>]}}
    - order_id: <orderId>
      item: '{{this.name}}'
      quantity: {{this.quantity}}
    {{/foreach}}
    """
    Examples:
      | orderId | itemsToBook                                                            |
      | 1       | [{"name": "t-shirt", "quantity": 1}, {"name": "short", "quantity": 2}] |