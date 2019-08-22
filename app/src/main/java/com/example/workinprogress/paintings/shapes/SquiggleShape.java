package com.example.workinprogress.paintings.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class SquiggleShape extends LineShape {

    private int[] firstLine;
    private int[] secondLine;

    /**
     * class to draw a shape that forms a squiggle
     *
     *       ,-.     ,-.
     * |`._ /   `._ /   `.
     * |     ,-.     ,-. |
     *  `._ /   `._ /   `|
     *
     * @param x1Start starting coordinates x1
     * @param y1Start starting coordinates y1
     * @param x2Start starting coordinates x2
     * @param y2Start starting coordinates y2
     * @param size size/length of shape
     * @param aRGBColor colour of shape
     */
    public SquiggleShape(int x1Start, int y1Start, int x2Start, int y2Start, int size, int[] aRGBColor) {
        super(x1Start, y1Start, x2Start, y2Start, size, aRGBColor);
    }

    /**
     * draws curves between coordinates defined in setfirstline()
     * @param canvas passed by view
     */
    public void draw(Canvas canvas) {
        setFirstLine();

        path.moveTo(x1Start, y1Start);
        path.lineTo(x2Start, y2Start);
        path.cubicTo(firstLine[0], firstLine[1], firstLine[2], firstLine[3], firstLine[4], firstLine[5]);
        path.cubicTo(firstLine[6], firstLine[7], firstLine[8], firstLine[9], firstLine[10], firstLine[11]);
        path.lineTo(x1End, y1End);
        path.cubicTo(secondLine[0], secondLine[1], secondLine[2], secondLine[3], secondLine[4], secondLine[5]);
        path.cubicTo(secondLine[6], secondLine[7], secondLine[8], secondLine[9], secondLine[10], secondLine[11]);

        canvas.drawPath(path, paint1);
    }

    /**
     * sets up the two lines to be drawn and the points at which they need to be moved to
     */
    private void setFirstLine() {

        firstLine = new int[]{x2Start, y2Start, x2Start, y2Start, x2Start, y2Start, x2End, y2End, x2End, y2End, x2End, y2End};
        secondLine = new int[]{x1End, y1End, x1End, y1End, x1End, y1End, x1Start, y1Start, x1Start, y1Start, x1Start, y1Start};

        int squiggleWidth = Math.round(((float) width / 2) + 40);
        int quarterSize = Math.round((float) size / 4);
        int halfSize = Math.round((float) size / 2);

        switch (startingDegree) {
            case 0:
                firstLine[0] += squiggleWidth;
                firstLine[1] -= quarterSize;
                firstLine[2] += squiggleWidth;
                firstLine[3] += quarterSize;

                firstLine[5] += halfSize;

                firstLine[6] += squiggleWidth;
                firstLine[7] -= quarterSize;
                firstLine[8] -= squiggleWidth;
                firstLine[9] -= quarterSize;

                secondLine[0] -= squiggleWidth;
                secondLine[1] -= quarterSize;
                secondLine[2] += squiggleWidth;
                secondLine[3] -= quarterSize;

                secondLine[5] += halfSize;

                secondLine[6] -= squiggleWidth;
                secondLine[7] += quarterSize;
                secondLine[8] += squiggleWidth;
                secondLine[9] += quarterSize;
                break;
            case 90:
                firstLine[0] -= quarterSize;
                firstLine[1] += squiggleWidth;
                firstLine[2] -= quarterSize;
                firstLine[3] -= squiggleWidth;
                firstLine[4] -= halfSize;

                firstLine[6] += quarterSize;
                firstLine[7] += squiggleWidth;
                firstLine[8] += quarterSize;
                firstLine[9] -= squiggleWidth;

                secondLine[0] += quarterSize;
                secondLine[1] -= squiggleWidth;
                secondLine[2] += quarterSize;
                secondLine[3] += squiggleWidth;
                secondLine[4] += halfSize;

                secondLine[6] -= quarterSize;
                secondLine[7] -= squiggleWidth;
                secondLine[8] -= quarterSize;
                secondLine[9] += squiggleWidth;
                break;
            case 180:
                firstLine[0] -= squiggleWidth;
                firstLine[1] -= quarterSize;
                firstLine[2] += squiggleWidth;
                firstLine[3] -= quarterSize;

                firstLine[5] -= halfSize;

                firstLine[6] -= squiggleWidth;
                firstLine[7] += quarterSize;
                firstLine[8] += squiggleWidth;
                firstLine[9] += quarterSize;

                secondLine[0] += squiggleWidth;
                secondLine[1] += quarterSize;
                secondLine[2] -= squiggleWidth;
                secondLine[3] += quarterSize;

                secondLine[5] += halfSize;

                secondLine[6] += squiggleWidth;
                secondLine[7] -= quarterSize;
                secondLine[8] -= squiggleWidth;
                secondLine[9] -= quarterSize;
                break;
            case 270:
                firstLine[0] += quarterSize;
                firstLine[1] += squiggleWidth;
                firstLine[2] += quarterSize;
                firstLine[3] -= squiggleWidth;
                firstLine[4] += halfSize;

                firstLine[6] -= quarterSize;
                firstLine[7] += squiggleWidth;
                firstLine[8] -= quarterSize;
                firstLine[9] -= squiggleWidth;

                secondLine[0] -= quarterSize;
                secondLine[1] -= squiggleWidth;
                secondLine[2] -= quarterSize;
                secondLine[3] += squiggleWidth;
                secondLine[4] -= halfSize;

                secondLine[6] += quarterSize;
                secondLine[7] -= squiggleWidth;
                secondLine[8] += quarterSize;
                secondLine[9] += squiggleWidth;
                break;
        }
    }

}
