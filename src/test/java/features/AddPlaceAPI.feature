Feature: Validating the Add Place API

  Scenario: To verify if user can able to add the place using add place api

    Given User have valid request URI and Contract
    When User hits "addPlaceResource" api using "POST" http method
    Then User wil get status code "200"
    And User should get "scope" equal to "APP" in response

