Feature: Some more examples of Tzatziki usage

  Scenario: we can reach external urls
    When we call "http://www.google.com"
    Then we receive a status 200


  Scenario: within guard working with call_and_assert
    Given that calling on "http://backend/asyncMock" will return a status 404
    And that after 2000ms calling on "http://backend/asyncMock" will return a status 200 and:
    """
      message: mocked async
    """
    Then getting on "http://backend/asyncMock" returns a status 404
    But within 20000ms getting on "http://backend/asyncMock" returns a status 200 and:
    """
      message: mocked async
    """

  Scenario: We can insert and assert db
    Given the booking table will contain:
    """
    order_id: "3.1"
    booked_items_with_price:
      - item_id: 1
        quantity: 10
        price: 1.99
      - item_id: 2
        quantity: 12
        price: 2.50
    """

    Then the booking table contains exactly:
    """
    id: "1"
    order_id: 3
    booked_items_with_price:
      - item_id: 1
        quantity: 10
        price: 1.99
      - item_id: 2
        quantity: 12
        price: 2.50
    """

  Scenario: we can set the log level of a specific class
    Given a com.decathlon.tzatziki.steps logger set to DEBUG
    When we log as DEBUG:
      """
      some lines
      """
    Then the logs contain:
      """
      - ?e .* some lines
      """

  Scenario: assert of fields using flags
    Given that user is a Map:
      """yml
      id: 1
      name: Bob
      uuid: c8eb85bc-c7fc-4586-9f91-c14e7c9d473e
      age: 20
      created: {{{[@10 mins ago]}}}
      """
    Then user.age == "?eq 20"
    Then user.age == "?== 20"
    Then user.age == "?gt 19"
    Then user.age == "?> 19"
    And user.age == "?ge 20"
    And user.age == "?>= 20"
    And user.age == "?lt 21"
    And user.age == "?< 21"
    And user.age == "?le 20"
    And user.age == "?<= 20"
    And user.age == "?not 0"
    And user.age == "?!= 0"
    And user.name == "?not null"
    And user.created == "?before {{@now}}"
    And user.created == "?after {{{[@20 mins ago]}}}"
    And user.name == "?base64 Qm9i"
    And user.uuid == "?isUUID"
    And user contains:
      """yml
      uuid: ?isUUID
      name: ?e B.*
      """