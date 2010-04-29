Feature: Test TestForm
    In order to ensure cuke4duke is working properly
    TestForm should respond to commands

    Scenario: Test filling basic fields
        Given a new TestForm with window "TestForm"
        When I fill in initialValueTF with "100"
            And I lose focus from initialValueTF
            And I select combo box calculationCB value "Future Value"
        Then initialValueTF should contain "100"
