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

    img = cv2.line(frame, (0,0), (width, height), (255, 0, 0), 10)
    img = cv2.line(img, (0,height), (width, 0), (0, 250, 0), 5)
    cv2.imshow('frame', img)

    if cv2.waitKey(1) == ord('q'):
        break
cap.release()
cv2.destroyAllWindows()