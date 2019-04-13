package com.example.abchar;

import android.util.Pair;

import com.google.common.graph.ImmutableValueGraph;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_32FC2;

public class Warper {


    private List<Mat> corners;

    public Warper(List<Mat> corners) {
        this.corners = corners;
    }
    public Warper() {}


    public Mat warp(Mat src, int height, int width){
        List<Point> markerCenters = this.getMarkerCenters();
        Size size = getWarpedSize(markerCenters);
        Mat pre_centers = Converters.vector_Point2f_to_Mat(markerCenters);
        Mat centers = new Mat();
        pre_centers.convertTo(centers,CV_32F);
        //System.out.println("AAAAAA" + centers.toString());
        Mat dst = createDst(size);
        //System.out.println("AAAAAA dst " + dst.toString());
        Mat M = Imgproc.getPerspectiveTransform(centers, dst);
        Imgproc.warpPerspective(src,dst,M,size);
        Mat resized = new Mat(height,width,CV_32FC2);
        Imgproc.resize(dst,resized,new Size(width,height));
        //Imgproc.cvtColor(resized,resized,Imgproc.COLOR_GRAY2RGBA);

        return resized;
    }

    public List<Point> getMarkerCenters() {
        double x,y;
        List<Point> centers = new ArrayList<>();
        double[] temp1, temp2;
        for (Mat corner : this.corners) {
            temp1 = corner.get(0, 0);
            temp2 = corner.get(0,2);
            x = (temp1[0] + temp2[0]) / 2;
            y = (temp1[1] + temp2[1]) / 2;
            Point p = new Point(x,y);
            centers.add(p);
        }
        return this.locate_points(centers);

    }

    public List<Point> locate_points(List<Point> cornerCenters){
        List<Double> diff = new ArrayList<>();
        List<Double> sum = new ArrayList<>();
        List<Point> result = new ArrayList<>();
        for( int i = 0; i < cornerCenters.size();i++){
            diff.add(cornerCenters.get(i).x - cornerCenters.get(i).y);
            sum.add(cornerCenters.get(i).x + cornerCenters.get(i).y);
            //System.out.println("AAAA" + String.valueOf(cornerCenters.get(i).x - cornerCenters.get(i).y)+ "-----+ " + String.valueOf(cornerCenters.get(i).x + cornerCenters.get(i).y));
        }
        result.add(cornerCenters.get(sum.indexOf(Collections.min(sum))));
        result.add(cornerCenters.get(diff.indexOf(Collections.min(diff))));
        result.add(cornerCenters.get(diff.indexOf(Collections.max(diff))));
        result.add(cornerCenters.get(sum.indexOf(Collections.max(sum))));
        return result;
    }

    public Size getWarpedSize(List<Point> points){
        double topWidth, bottomWidth, leftHeight, rightHeight;
        topWidth =  Math.sqrt(Math.pow(points.get(0).x - points.get(1).x,2) + Math.pow(points.get(0).y - points.get(1).y,2));
        bottomWidth = Math.sqrt(Math.pow(points.get(2).x - points.get(3).x,2) + Math.pow(points.get(2).y - points.get(3).y,2));
        leftHeight = Math.sqrt(Math.pow(points.get(0).x - points.get(2).x,2) + Math.pow(points.get(0).y - points.get(2).y,2));
        rightHeight =  Math.sqrt(Math.pow(points.get(1).x - points.get(3).x,2) + Math.pow(points.get(1).y - points.get(1).y,2));
        //System.out.println("AAAA" + String.valueOf(Math.max(topWidth, bottomWidth)) + "|||||||" + String.valueOf(Math.max(leftHeight, rightHeight)));
        return new Size(Math.max(topWidth, bottomWidth), Math.max(leftHeight, rightHeight));
    }

    public Mat createDst(Size size){
        Point p1 = new Point(0,0);
        Point p2 = new Point(size.width - 1, 0);
        Point p3 = new Point(0, size.height -1);
        Point p4 = new Point(size.width -1, size.height -1);
        List<Point> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        return Converters.vector_Point2f_to_Mat(points);

    }
    public List<Mat> getCorners() {
        return corners;
    }

    public void setCorners(List<Mat> corners) {
        this.corners = corners;
    }
}
