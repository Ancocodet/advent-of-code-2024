package com.ancozockt.advent.general;

import lombok.*;

import java.util.List;
import java.util.function.UnaryOperator;

@Data
@Getter @Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Coordinate {

    public final static List<UnaryOperator<Coordinate>> horizontal = List.of(Coordinate::transformLeft, Coordinate::transformRight);
    public final static List<UnaryOperator<Coordinate>> vertical = List.of(Coordinate::transformUp, Coordinate::transformDown);

    public int x;
    public int y;

    public Coordinate(Coordinate coordinate) {
        this.x = coordinate.x;
        this.y = coordinate.y;
    }

    public List<Coordinate> neighbours() {
        return List.of(
            transform(this, up()),
            transform(this, down()),
            transform(this, left()),
            transform(this, right())
        );
    }

    public void transform(Coordinate direction) {
        this.x += direction.x;
        this.y += direction.y;
    }


    Coordinate transformUp() {
        return transform(this, up());
    }

    Coordinate transformDown() {
        return transform(this, down());
    }

    Coordinate transformLeft() {
        return transform(this, left());
    }

    Coordinate transformRight() {
        return transform(this, right());
    }

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

    public static Coordinate substract(Coordinate leftOperand, Coordinate rightOperand) {
        int x = leftOperand.x - rightOperand.x;
        int y = leftOperand.y - rightOperand.y;
        return new Coordinate(x,y);
    }

    public static Coordinate direction(Coordinate p, Coordinate q) {

        return Coordinate.substract(p, q);
    }

}
