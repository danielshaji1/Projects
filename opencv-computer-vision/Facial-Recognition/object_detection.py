'''
Template matching stock images using template images, size of template and
base image are VERY important.
'''
import cv2
import numpy as np
#target and template images should be around the same size so it's more accurate to find in the base image
#pixel sizes matter

#0 = gray_scale
img = cv2.imread('soccer/soccer_practice.jpg', 0)
template = cv2.imread('soccer/shoe.PNG', 0)
#this is a 2D array not 3D since there is no other BGR values, it is in gray scale so BGR is all or nothing
h, w = template.shape

#when deploying irl, try all algorithms and see which is most efficient
methods = [cv2.TM_CCOEFF, cv2.TM_CCOEFF_NORMED, cv2.TM_CCORR,
            cv2.TM_CCORR_NORMED, cv2.TM_SQDIFF, cv2.TM_SQDIFF_NORMED]

#trying all methods to see which one is the best
for method in methods:
    img2 = img.copy()

    result = cv2.matchTemplate(img2, template, method)
    min_val, max_val, min_loc, max_loc = cv2.minMaxLoc(result)
    
    if method in [cv2.TM_SQDIFF, cv2.TM_SQDIFF_NORMED]:
        location = min_loc
    else:
        location = max_loc

    bottom_right = (location[0] + w,  location[1] + h)

    cv2.rectangle(img2, location, bottom_right, 255, 5)
    cv2.imshow('Match',img2)
    cv2.waitKey(0)
    cv2.destroyAllWindows()
