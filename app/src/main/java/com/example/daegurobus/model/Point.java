package com.example.daegurobus.model;

import java.util.List;

public class Point {

    public double lon, lat;

    public String closeLon;
    public String closeLat;

    public Point(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    // 다른 점과의 거리 계산
    public double distanceTo(Point other) {
        return Math.sqrt(Math.pow(this.lon - other.lon, 2) + Math.pow(this.lat - other.lat, 2));
    }


    public Point findClosestPoint(Point current, List<Point> points) {
        Point closest = null;
        double minDistance = Double.MAX_VALUE;

        for (Point point : points) {
            double distance = current.distanceTo(point);
            if (distance < minDistance) {
                minDistance = distance;
                closest = point;
            }
        }
        setCloseLon(Double.toString(closest.lon));
        setCloseLat(Double.toString(closest.lat));


        return closest;
    }

    public String getCloseLon() {
        return closeLon;
    }

    public void setCloseLon(String closeLon) {
        this.closeLon = closeLon;
    }

    public String getCloseLat() {
        return closeLat;
    }

    public void setCloseLat(String closeLat) {
        this.closeLat = closeLat;
    }
}
