package com.example.realtimedatabasereusuablecodedoc

class DistanceComparison : Comparator<LocationDC> {
    override fun compare(p0: LocationDC?, p1: LocationDC?) : Int {
        if (p0 != null && p1 != null) {
            var distance1: Float? = p0.distanceFromUser
            var distance2: Float? = p1.distanceFromUser
            if (distance1 != null && distance2 != null) {
                return distance1.compareTo(distance2)
            }
        }
        return -100
    }
}