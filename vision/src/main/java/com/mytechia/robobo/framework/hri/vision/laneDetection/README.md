# Robobo lane detection module

The Robobo lane detection uses OpenCV to detect lanes:
The standard module uses the lineDetector module and returns the two straight lines (see Format #1).
The advanced module uses only the OpenCV camera to find two lines by fitting second degree polynomials in a birdview from the original image.

## Format #1:
A Mat containing the two lines with the x,y coordinates for the starting and ending point of the line
- cor1x: First corner x coordinate
- cor1y: First corner y coordinate
- cor2x: Second corner x coordinate
- cor2y: Second corner y coordinate

## Format #2:
Two Line objects and a transformation matrix (3x3) used to transform the plane/image where the lines could be projected so it fits with the unwarped image.
To draw the line, there's the Line.draw() function (see more at the Javadoc), or could use the last_fit_pixel member variable (double array with each coefficient).

## Notes
The advanced module is recommended if possible.
The advanced module detect white and yellow lines.

The standard module detects any line.

To import the module in your Robobo application you must declare the module in the modules.properties file:

`robobo.module.#MODULE_NUMBER#=com.mytechia.robobo.framework.hri.vision.laneDetection.opencv.OpencvAdvancedLaneDetectionModule`

or

`robobo.module.#MODULE_NUMBER#=com.mytechia.robobo.framework.hri.vision.lineDetection.opencv.OpencvLineDetectionModule`
`robobo.module.#MODULE_NUMBER#=com.mytechia.robobo.framework.hri.vision.laneDetection.opencv.OpencvLaneDetectionModule`