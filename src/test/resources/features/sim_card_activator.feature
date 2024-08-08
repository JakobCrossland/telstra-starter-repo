Feature: SIM Card Activation

  Scenario: Successful SIM card activation
    Given the SIM card actuator service is running
    When I send a POST request to activate SIM card with ICCID "1255789453849037777"
    Then the response should indicate success
    And I should be able to query the transaction by ID and see the activation status as successful

  Scenario: Failed SIM card activation
    Given the SIM card actuator service is running
    When I send a POST request to activate SIM card with ICCID "8944500102198304826"
    Then the response should indicate failure
    And I should be able to query the transaction by ID and see the activation status as failed
