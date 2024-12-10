package com.ancozockt.advent.general;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
@AllArgsConstructor
public class Coordinate {

    public final int x;
    public final int y;

    public static Coordinate up() {
        return new Coordinate(0, 1);
    }

    public static Coordinate down() {
        return new Coordinate(0, -1);
    }

    public static Coordinate left() {
        return new Coordinate(-1, 0);
    }

    public static Coordinate right() {
        return new Coordinate(1, 0);
    }

    public static Coordinate transform(Coordinate cord, Coordinate direction) {
        return new Coordinate(cord.x + direction.x,
                        cord.y + direction.y);
    }

}
