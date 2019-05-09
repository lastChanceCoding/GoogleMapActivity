Project:  Where Did I Park My Car?
Author:   Jentry Maxwell and David Baker
Contributions:  Portions of code and code framing provided by Steve Osburn
Class:  CIS165, Spring 2019, Final Project

Changelog
    2019-04-30 initial commit to Git and GitHub
    2019-05-08 rebase at version 17 into GitHub
    
 Notes:
 1.  This was a class example, so not fully functional for all API levels.  Tested on API Level 28, will have issues at API level < 23, so further code development needed for older devices.
 2. Better responsive design:  although it displays properly on both of our phones and on the emulators we selected, more testing would be needed for other screen sizes and resolutions. 
    
Overview:
This app easily remembers where you parked, and allows you to easily make your way back to your vehicle.  Although we demonstrate it's usefulness on a campus setting, it is also applicable to other situations where you need to find your way back... the mall, the concert, the stadium, the trailhead.

Benefits:
	1. Easy to use, intuitive interface, very simple to operate
	2. Relies only on a good GPS signal, but is augmented with a Google feature map underlay that aids with recognition of surrounding structures.

Main Operating Screen:  After clicking Continue on the Welcome Screen, we begin tracking your present location (red), and stay centered as you move.  Map zoom and scroll are enabled.  When you click Current Location, we'll remember that location in the database.  
