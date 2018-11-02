# internship-task-2018

Write a component which receives messages containing user agent string and http response code. The component provides two read operations:

* query for the snapshot of last 100 received messages
* query for the number of messages with an error http status code within the last 100 requests

For both read operations messages older than 5 min should not be taken into consideration (e.g. if there were only 30 messages during last 5 minutes then return those 30).
There are multiple (possibly lots of) writers and readers.

Service API:
```
    void add(Message message)

    Snapshot snapshot()

    long numberOfErrorMessages()
```