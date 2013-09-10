package net.gmx.nosefish.fishylib.worldmath;

/**
 * Home of the runOnCircle method and family
 * 
 * @author Stefan Steinheimer (nosefish)
 *
 */
public final class CircleRunner {

	/**
	 * Interface to use with runOnCircle
	 *
	 */
	public static interface IRunnableXY {
		public void run(int x, int y);
	}
	

    /**
	 * Runs <code>IRunnable</code> toRun for every point of a circle,
	 * determined by the Midpoint-Algorithm.
	 * 
	 * @param centreX
	 *     centre point x-value
	 *     
	 * @param centreY
	 *     centre point y-value
	 *     
	 * @param radius
	 *     radius of the circle
	 *     
	 * @param fill
	 *     if <code>false</code> the task runs only on the outline,
	 *     if <code>true</code> it is executed on interior points, too
	 *     
	 * @param toRun
	 *     the task to run
	 */
	public static void runOnCircle(int centreX, int centreY, int radius, boolean fill, IRunnableXY toRun) {
	    int x = 0;
	    int y = radius;
	    int p = (5 - radius * 4) / 4;
	    if (fill) {
	    	runOnFilledCircle(centreX, centreY, x, y, toRun);
	    } else {
	    	runOnCircleOutline(centreX, centreY, x, y, toRun);
	    }
	    while (x < y) {
	        x++;
	        if (p < 0) {
	            p += 2 * x + 1;
	        } else {
	            y--;
	            p += 2 * (x - y) + 1;
	        }
	        if (fill) {
	        	runOnFilledCircle(centreX, centreY, x, y, toRun);
	        } else {
	        	runOnCircleOutline(centreX, centreY, x, y, toRun);
	        }
	    }
	}

	private static void runOnCircleOutline(
    		int centreX, int centreY,
    		int x, int y,
    		IRunnableXY toRun) {
    	
    	if (x == 0) {
            toRun.run(centreX,     centreY + y);
            toRun.run(centreX,     centreY - y);
            toRun.run(centreX + y, centreY    );
            toRun.run(centreX - y, centreY    );
        } else if (x == y) {
        	toRun.run(centreX + x,  centreY + y);
        	toRun.run(centreX - x,  centreY + y);
        	toRun.run(centreX + x,  centreY - y);
        	toRun.run(centreX - x,  centreY - y);
        } else if (x < y) {
        	toRun.run(centreX + x,  centreY + y);
        	toRun.run(centreX - x,  centreY + y);
        	toRun.run(centreX + x,  centreY - y);
        	toRun.run(centreX - x,  centreY - y);
        	toRun.run(centreX + y,  centreY + x);
        	toRun.run(centreX - y,  centreY + x);
        	toRun.run(centreX + y,  centreY - x);
        	toRun.run(centreX - y,  centreY - x);
        }
    }
    
    private static void runOnFilledCircle(
    		int centreX, int centreY,
    		int x, int y,
    		IRunnableXY toRun) {
    	if (x == 0) {
    		// centre point
        	toRun.run(centreX, centreY);
    		// cardinals
    		for (int d = 1; d <= y; d++) {
    			toRun.run(centreX,     centreY + d);
    			toRun.run(centreX,     centreY - d);
    			toRun.run(centreX + d, centreY    );
    			toRun.run(centreX - d, centreY    );
    		}
        } else if (x == y) {
        	// "diagonals"
        	for (int d = 1; d <= y; d++) {
        		toRun.run(centreX + d, centreY + d);
        		toRun.run(centreX - d, centreY + d);
        		toRun.run(centreX + d, centreY - d);
        		toRun.run(centreX - d, centreY - d);
        	}
        } else if (x < y) {
        	// all the rest
        	for (int d = x + 1; d <= y; d++) { 
        	toRun.run(centreX + x, centreY + d);
        	toRun.run(centreX - x, centreY + d);
        	toRun.run(centreX + x, centreY - d);
        	toRun.run(centreX - x, centreY - d);
        	toRun.run(centreX + d, centreY + x);
        	toRun.run(centreX - d, centreY + x);
        	toRun.run(centreX + d, centreY - x);
        	toRun.run(centreX - d, centreY - x);
        	}
        	if (x + 1 == y) {
            	// "diagonals"
            	for (int d = 1; d < y; d++) {
            		toRun.run(centreX + d,  centreY + d);
            		toRun.run(centreX - d,  centreY + d);
            		toRun.run(centreX + d,  centreY - d);
            		toRun.run(centreX - d,  centreY - d);
            	}
        	}
        }
    }
    
}
