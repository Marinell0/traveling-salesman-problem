# **Travelling salesman heuristic solver**

This project is focused on solving the traveling salesman problem with an heuristic aproach, finding the shortest path to make a cicle on the points given, taking in consideration the radius of each point.

## **How to use**

Here's the help given by the jar file. It explains each command you can give in the console.

Usage: java -jar 'file'.jar [-a antenna-quantity] [-s [seed]] [-r repetitions] [-n num_points] [-h] [-c count] file_name

Options:
        -a antenna-quantity     Specifies how many antennas to look for minimum path.
                                Default is 30.
        -s [seed]               Shuffle the antennas used instead of taking the same antennas
                                on order from the file. If seed is present, uses the seed as the random factor.
        -r repetitions          Number of times to do a Hillclimb with removed intersections.
                                Default is 10 repetitions.
        -n num-points           Number of points > 1 inside antenna radius to fit the path.
                                If not present, fit the path only to the antenna origin.
                                Default is 10.
        -h                      If present, uses hillclimb looking for random points inside the radius.
                                Can only be used if -r is present.
        -c count                Number of cores to use.
                                Default is total cores of your system - 2.