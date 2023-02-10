Feature: [ORDER BOOKING] We can book items for an order using /warehouses/{warehouse}/orders providing the items to book along with their quantity.
  The warehouse country should be extracted from the first two warehouse characters
  It should then:
  - call /stock-api/{warehouse}/book with the given items
  - fetch price from /price-api/items/{item_id} for every item for the warehouse country
  - add a row to booking table with the order id, booked items and the price when the call was done
  - send an OrderStatusUpdate in order_status_update topic
  - return to the caller the booked items along with their price