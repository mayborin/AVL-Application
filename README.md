# RBT-Application
>###Problem description

Implement an event counter using red-black tree. Each event has one primary key ```ID``` and several feature fields. As a simple
displaying of how to implement a red-black tree, this applicaton functions as a event count. The event has another ```COUNT``` field.
The event counter stores only those ID's whose COUNT is positive. Once a COUNT drops below 1, the related ID is removed.
Initially, the program must build red-black tree from a sorted list of n event (n paris of (ID, COUNT) in ascending order of ID)
in O(n) time.
The counter also should support the following operations in the specified time complexity.

Command | Description | Time complexity
--- | --- | ---
Increase(ID,m) | Increase the COUNT of the event ID by m. If ID is not present, insert it. Print the COUNT of ID after the addition. | O(logn)
Reduce(ID,m) | Decrease the COUNT of the ID by m. If the ID's COUNT drop below 1, remove the ID from the counter. Print the COUNT of ID after the deletion, or 0 if ID is removed or not present. | o(logn)
Count(ID) | Print the COUNT of ID. If not present, print 0. | O(logn)
InRange(ID1, ID2) | Print the total count for IDs between ID1 and ID2 inclusively. ID1 should not be larger than ID2. | O(logn+s) where s is the number of IDs in the range
Next(ID) | Print the ID and the COUNT of the event with the lowest ID that's greater than ID. Print "0, 0" if there is no such ID. | o(logn)
Previous(ID) | Print the ID and the Count of the event with the greatest key that's smaller than ID. Print "0, 0" if no such ID. | O(logn)

The initial data and querys are read from files.

Input data format:
```
n
ID1 COUNT1
ID2 COUNT2
...
IDn COUNTn
```
Query data format:

Example : `inrange 3 5`

***

API:
