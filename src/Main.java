import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class Main {



    public static void main(String[] args) {
        MazeIO mazeIO = new MazeIO();
        try {
            Maze maze = mazeIO.loadMaze("mazes\\maze4.txt");
            List<Point> path = new MazeSolver().shortestPath(maze);

            mazeIO.printMazeWithPath(maze, path);
            System.out.println(path.size());
            mazeIO.printMazePng(maze);

            mazeIO.printMazePathDetectSolPng(maze, path);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
