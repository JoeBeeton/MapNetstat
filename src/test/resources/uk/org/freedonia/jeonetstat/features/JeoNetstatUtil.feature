Feature: Run JeoNetStatUtil on Linux
  Scenario: Run JeoNetStatUtil on Linux with Local Connections turned off
    Given The Local Connection flag is set to "false"
      And The Protocol Options is set to "TCP"
      And JeoNetStatUtil.getCurrentConnections is called
     Then the following results are returned
      | pid  | Process Name | ip          |port| Memory | Country | City   | Latitude | Longitude | 
      | 1234 | Chrome       | 142.168.1.1 |443| 123456 | UK      | London | 123.321  | 321.123   | 

  