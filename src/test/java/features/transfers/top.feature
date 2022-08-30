@post/transfers
Feature: POST/transfers

  @tra1
  Scenario: Make a successful top transfer
    Given a payload:
      | source_account_id     | 2460                             |
      | source_user_id        | auth0\|62e05df243e9885859dc87c7  |
      | amount                | 2                                |
      | currency              | JMD                              |
      | comments              | top transfer                     |
      | transfer_type         | TOP                              |
      | external_reference_id | 3336                             |
      | account_reference     | 234561049                        |
      | bank_identifier       | NCB                              |

    When perform a POST to transfers
    Then verify that the status code it is "201"
    And all the data in the response body is correct and valid
    And the team.nautilus.event.transfer.ncb.topup event was generated on the ncb_operations topic
    And the team.nautilus.event.balance.ncb.topup event was generated on the balance topic
    And the team.nautilus.event.transfer.balance.update event was generated on the balance topic