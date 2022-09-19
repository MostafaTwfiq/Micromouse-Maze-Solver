import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MazeIO {


    public Maze loadMaze(String mazePath) throws Exception {

        File file = new File(mazePath);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        Point start = null, end = null;

        List<List<Boolean>> maze = new ArrayList<>();
        int count = 0;
        while ((st = br.readLine()) != null) {
            ArrayList<Boolean> currRow = new ArrayList<>();
            for (int i = 0; i < st.length(); i++) {
                char c = st.charAt(i);
                switch (c) {
                    case 'A':
                        if (start == null) {
                            start = new Point(i, count);
                            currRow.add(false);
                        } else
                            throw new Exception("There can't be more than one start point.");
                        break;
                    case 'B':
                        if (end == null) {
                            end = new Point(i, count);
                            currRow.add(false);
                        } else
                            throw new Exception("There can't be more than one end point.");
                        break;
                    case '#':
                        currRow.add(true);
                        break;
                    case ' ':
                        currRow.add(false);
                        break;
                    default:
                        throw new Exception("File is not valid.");

                }
            }
            maze.add(currRow);
            count++;
        }

        return new Maze(start, end, maze);

    }


    public void printMaze(Maze maze) {
        List<List<Boolean>> mazeMap = maze.getMaze();
        for (int i = 0; i < mazeMap.size(); i++) {
            for (int j = 0; j < mazeMap.get(i).size(); j++) {
                if (mazeMap.get(i).get(j)) {
                    System.out.print("█");
                } else if (j == maze.getStart().x && i == maze.getStart().y) {
                    System.out.print("A");
                } else if (j == maze.getEnd().x && i == maze.getEnd().y) {
                    System.out.print("B");
                } else
                    System.out.print(" ");
            }
            System.out.println();
        }
    }

    public void printMazeWithPath(Maze maze, List<Point> path) {
        List<List<Boolean>> mazeMap = maze.getMaze();
        for (int i = 0; i < mazeMap.size(); i++) {
            for (int j = 0; j < mazeMap.get(i).size(); j++) {
                if (mazeMap.get(i).get(j)) {
                    System.out.print("█");
                } else if (j == maze.getStart().x && i == maze.getStart().y) {
                    System.out.print("A");
                } else if (j == maze.getEnd().x && i == maze.getEnd().y) {
                    System.out.print("B");
                } else if (path.contains(new Point(j, i))) {
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public void printMazePng(Maze maze) throws Exception{
        int maxY = maze.getMaze().size();
        int maxX = 0;
        for (int i = 0; i < maze.getMaze().size(); i++)
            maxX = Math.max(maze.getMaze().get(i).size(), maxX);


        final int SQUARE_SIDE_LENGTH = 50;
        BufferedImage bufferedImageMaze = new BufferedImage((maxX) * SQUARE_SIDE_LENGTH, (maxY) * SQUARE_SIDE_LENGTH, BufferedImage.TYPE_INT_RGB);

        Graphics mazeGraphics = bufferedImageMaze.createGraphics();

        mazeGraphics.setColor(Color.black);
        mazeGraphics.fillRect(0, 0, (maxX) * SQUARE_SIDE_LENGTH, (maxY) * SQUARE_SIDE_LENGTH);

        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maze.getMaze().get(i).size(); j++) {
                mazeGraphics.setColor(Color.black);
                mazeGraphics.fillRect(j * SQUARE_SIDE_LENGTH, i * SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH);
                mazeGraphics.setColor(Color.black);

                if (maze.getMaze().get(i).get(j)) {
                    mazeGraphics.setColor(Color.darkGray);
                } else if (j == maze.getStart().x && i == maze.getStart().y) {
                    mazeGraphics.setColor(Color.green);
                } else if (j == maze.getEnd().x && i == maze.getEnd().y) {
                    mazeGraphics.setColor(Color.red);
                } else {
                    mazeGraphics.setColor(Color.lightGray);
                }

                mazeGraphics.fillRect(1 + j * SQUARE_SIDE_LENGTH, 1 + i * SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH - 2, SQUARE_SIDE_LENGTH - 2);

            }

        }
        ImageIO.write(bufferedImageMaze, "jpg", new File("Maze.jpg"));
    }

    public void printMazeSolPng(Maze maze, List<Point> path) throws Exception{
        int maxY = maze.getMaze().size();
        int maxX = 0;
        for (int i = 0; i < maze.getMaze().size(); i++)
            maxX = Math.max(maze.getMaze().get(i).size(), maxX);


        final int SQUARE_SIDE_LENGTH = 50;
        BufferedImage bufferedImageSolution = new BufferedImage((maxX) * SQUARE_SIDE_LENGTH, (maxY) * SQUARE_SIDE_LENGTH, BufferedImage.TYPE_INT_RGB);

        Graphics solutionGraphics = bufferedImageSolution.createGraphics();

        solutionGraphics.setColor(Color.black);
        solutionGraphics.fillRect(0, 0, (maxX) * SQUARE_SIDE_LENGTH, (maxY) * SQUARE_SIDE_LENGTH);

        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maze.getMaze().get(i).size(); j++) {
                solutionGraphics.setColor(Color.black);
                solutionGraphics.fillRect(j * SQUARE_SIDE_LENGTH, i * SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH);
                solutionGraphics.setColor(Color.black);

                if (maze.getMaze().get(i).get(j)) {
                    solutionGraphics.setColor(Color.darkGray);
                } else if (j == maze.getStart().x && i == maze.getStart().y) {
                    solutionGraphics.setColor(Color.green);
                } else if (j == maze.getEnd().x && i == maze.getEnd().y) {
                    solutionGraphics.setColor(Color.red);
                } else if (path.contains(new Point(j, i))) {
                    solutionGraphics.setColor(Color.blue);
                } else {
                    solutionGraphics.setColor(Color.lightGray);
                }

                solutionGraphics.fillRect(1 + j * SQUARE_SIDE_LENGTH, 1 + i * SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH - 2, SQUARE_SIDE_LENGTH - 2);

            }

        }
        ImageIO.write(bufferedImageSolution, "jpg", new File("MazeSolved.jpg"));
    }



    public void printMazePathDetectSolPng(Maze maze, List<Point> path) throws Exception{
        int maxY = maze.getMaze().size();
        int maxX = 0;
        for (int i = 0; i < maze.getMaze().size(); i++)
            maxX = Math.max(maze.getMaze().get(i).size(), maxX);

        HashMap<Point, MouseMovements> mouseMovements = new MazeSolver().detectMouseMovements(path);

        final int SQUARE_SIDE_LENGTH = 50;
        BufferedImage bufferedImageSolution = new BufferedImage((maxX) * SQUARE_SIDE_LENGTH, (maxY) * SQUARE_SIDE_LENGTH, BufferedImage.TYPE_INT_RGB);

        Graphics solutionGraphics = bufferedImageSolution.createGraphics();

        solutionGraphics.setColor(Color.black);
        solutionGraphics.fillRect(0, 0, (maxX) * SQUARE_SIDE_LENGTH, (maxY) * SQUARE_SIDE_LENGTH);

        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maze.getMaze().get(i).size(); j++) {
                solutionGraphics.setColor(Color.black);
                solutionGraphics.fillRect(j * SQUARE_SIDE_LENGTH, i * SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH);
                solutionGraphics.setColor(Color.black);

                if (maze.getMaze().get(i).get(j)) {
                    solutionGraphics.setColor(Color.darkGray);
                } else if (j == maze.getStart().x && i == maze.getStart().y) {
                    solutionGraphics.setColor(Color.green);
                } else if (j == maze.getEnd().x && i == maze.getEnd().y) {
                    solutionGraphics.setColor(Color.red);
                } else if (path.contains(new Point(j, i))) {
                    solutionGraphics.setColor(mouseMovements.get(new Point(j, i)).getColor());

                } else {
                    solutionGraphics.setColor(Color.lightGray);
                }

                solutionGraphics.fillRect(1 + j * SQUARE_SIDE_LENGTH, 1 + i * SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH - 2, SQUARE_SIDE_LENGTH - 2);

            }

        }
        ImageIO.write(bufferedImageSolution, "jpg", new File("MazeSolved.jpg"));
    }

}
