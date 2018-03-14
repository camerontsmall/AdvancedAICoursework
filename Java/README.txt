Config.txt file has the following settings:

Line 1 - Number of times to repeat everything
Line 2 - Comma seperated numbers representing number of epochs to run
Line 3 - Comma seperated numbers representing number of hidden nodes to try

eg.

4
500,700
3,5

will run 

500 epochs with 3 nodes
500 epochs with 5 nodes
900 epochs with 3 nodes
900 epochs with 5 nodes

And repeat that four times

Remember to make new folders in this directory named "export" and "report" for the report files to be placed in
