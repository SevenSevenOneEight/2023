# 7718's 2023 Code
7718's code for the 2023 FRC season, includes apriltag detection using PhotonVision, easy changes to the auto using PathPlanner, and more.
Older code is in the Main branch, new code can be found in the Refactor branch.
## Key Aspects


### Pathplanner utilization
Pathplanner allows for easy adjustment of the auto path of the robot using the chooser included, flexible and easily changable

### Constants file
By keeping key variables in one folder, we keep our code organized.

![image](https://user-images.githubusercontent.com/123131688/231229621-89ed5af8-8906-400c-ba38-56adc0b205ea.png)


### Super classes
Super classes used for merging sim and real code, simulations let you practice driving and work on your code without a robot.

### Apriltag detection
Multiple cameras running PhotonVision enable our robot to utilize apriltags, and more accurately position itself on the game field. [Not fully implemented] 

### Advantagekit
using Advantagekit, we can log our matches and view them using In Control. A variety of statistics can be tracked using this tool.

![image](https://user-images.githubusercontent.com/123131688/231228021-cefb4148-5762-41a6-bc80-39448ebc2857.png)
In Control with an example log open

### Dual Controller setup
Using two controllers, controls are simlified between two drivers making training drivers much easier and simpler

## Other stuff
### Setup
Super simple, just download libraries, assign can ID's and edit/add/remove anything you need.

![image](https://user-images.githubusercontent.com/123131688/231228707-cd51c208-4b70-4395-9f2e-732d498fc8b2.png)



#### Extras

 Related to NE's Java framework accesible at https://github.com/NE-Robotics/Java-Framework
 
  As mentioned above, the older code is in the Main branch, new code explained above can be found in the Refactor branch.
 
 Thanks Daniel! :)
