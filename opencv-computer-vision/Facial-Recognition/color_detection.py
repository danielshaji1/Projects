import numpy as np
import cv2

# 0 is one webcame, 1 is more, etc. You can also use mp4 video, and rather than 0, put the file name/path
cap = cv2.VideoCapture(0)

while True:
    ret, frame = cap.read()
    #by default they come as float, so convert to int.
    #each number is a different property of the frame, 3=width 4=height, look at documentation for more
    width = int(cap.get(3))
    height = int(cap.get(4))

    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)


    cv2.imshow('frame', hsv)

    if cv2.waitKey(1) == ord('q'):
        break
cap.release()
cv2.destroyAllWindows()