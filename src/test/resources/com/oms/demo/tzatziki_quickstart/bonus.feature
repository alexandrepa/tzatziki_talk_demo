Feature: Some more examples of Tzatziki usage

  Background:
    * aVariable is "a-simple-var"

  Scenario: We can catch exceptions
    Then a totallyExpectedException java.lang.Exception is thrown when aBadVariable is:
    """
    {{[aVariable.substring(42, 42)]}}
    """

    And totallyExpectedException.class == "com.github.jknack.handlebars.HandlebarsException"
    And totallyExpectedException.message is equal to "?e .*StringIndexOutOfBoundsException.*Range \[42, 42\) out of bounds for length 12[\s\S]*"

    And totallyExpectedException.cause.getClass == "java.lang.StringIndexOutOfBoundsException"
    And totallyExpectedException.cause.message == "Range [42, 42) out of bounds for length 12"

  Scenario: We can also do inverted assertions
    Given that splitVar is "{{[aVariable.split(-)]}}"
    Then it is not true that a java.lang.Exception is thrown when aGoodVariable is:
    """
    aWord: {{[splitVar.0]}}
    simpleWord: {{[aVariable.substring(2, 8)]}}
    varWord: {{[splitVar.2]}}
    """
    And aGoodVariable is equal to:
    """
    aWord: a
    simpleWord: simple
    varWord: var
    """
    And splitVar contains:
      | a | simple | var |