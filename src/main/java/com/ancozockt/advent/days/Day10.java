
package com.ancozockt.advent.days;

import com.ancozockt.advent.general.Coordinate;
import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AInputData(day = 10, year = 2024)
public class Day10 implements IAdventDay {

    private int width;
    private int height;

    private TopographicTile[][] map;
    private final Set<TopographicTile> reachedTiles = new HashSet<>();

    @Override
    public String part1(IInputHelper inputHelper) {
        TopographicTile[][] map = parseInput(inputHelper);

        List<TopographicTile> initialTiles = new ArrayList<>();
        for (TopographicTile[] tiles : map) {
            for (TopographicTile tile : tiles) {
                if (tile.getHeight() == 0) {
                    initialTiles.add(tile);
                }
            }
        }

        long result = 0;
        for (TopographicTile tile : initialTiles) {
            result += findPath(tile, -1, false);
            reachedTiles.clear();
        }

        return result + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        TopographicTile[][] map = parseInput(inputHelper);

        List<TopographicTile> initialTiles = new ArrayList<>();
        for (TopographicTile[] tiles : map) {
            for (TopographicTile tile : tiles) {
                if (tile.getHeight() == 0) {
                    initialTiles.add(tile);
                }
            }
        }

        long result = 0;
        for (TopographicTile tile : initialTiles) {
            result += findPath(tile, -1, true);
            reachedTiles.clear();
        }

        return result + "";
    }

    private long findPath(TopographicTile tile, int previousHeight, boolean rating) {

        if(tile.isVisited()) {
            return 0;
        }

        if(tile.getHeight()-previousHeight != 1) {
            return 0;
        }

        if((tile.getHeight() == 9) && !rating && !reachedTiles.contains(tile)){
            reachedTiles.add(tile);
            return 1;
        } else if((tile.getHeight() == 9) && rating) {
            return 1;
        }

        long result = 0;

        tile.setVisited(true);

        Set<TopographicTile> adjacentTiles = getAdjacentTiles(tile);

        for(TopographicTile adjacentTile : adjacentTiles) {
            result += findPath(adjacentTile, tile.getHeight(), rating);
        }

        tile.setVisited(false);

        return result;

    }

    private TopographicTile[][] parseInput(IInputHelper input) {
        String[] lines = input.getInputAsStream().toArray(String[]::new);

        height = lines.length;
        width = lines[0].length();

        map = new TopographicTile[lines.length][lines[0].length()];
        for (int y = 0; y < lines.length; y++){
            String[] line = lines[y].split("");
            for (int x = 0; x < line.length; x++) {
                map[y][x] = new TopographicTile(new Coordinate(x, y),
                                                Integer.parseInt(line[x]));
            }
        }

        return map;
    }

    private Set<TopographicTile> getAdjacentTiles(TopographicTile tile) {
        Set<TopographicTile> adjacentTiles = new HashSet<>();

        Coordinate initialPos = tile.getCord();

        Coordinate nextPos = Coordinate.transform(initialPos, Coordinate.left());
        if(isInbounds(nextPos)) {
            adjacentTiles.add(map[nextPos.getY()][nextPos.getX()]);
        }

        nextPos = Coordinate.transform(initialPos, Coordinate.right());
        if(isInbounds(nextPos)) {
            adjacentTiles.add(map[nextPos.getY()][nextPos.getX()]);
        }

        nextPos = Coordinate.transform(initialPos, Coordinate.up());
        if(isInbounds(nextPos)) {
            adjacentTiles.add(map[nextPos.getY()][nextPos.getX()]);
        }

        nextPos = Coordinate.transform(initialPos, Coordinate.down());
        if(isInbounds(nextPos)) {
            adjacentTiles.add(map[nextPos.getY()][nextPos.getX()]);
        }

        return adjacentTiles;
    }

    private boolean isInbounds(Coordinate pos) {
        return (pos.getY() >= 0 && pos.getY() < height && pos.getX() >= 0 && pos.getX() < width);
    }

    @Getter @Setter
    @RequiredArgsConstructor
    static class TopographicTile {
        private boolean visited = false;
        private final Coordinate cord;
        private final int height;
    }
}
