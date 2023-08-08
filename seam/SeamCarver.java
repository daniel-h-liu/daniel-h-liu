import edu.princeton.cs.algs4.Picture;

import java.util.Arrays;

public class SeamCarver {

    private int width; // current width of picture
    private int height; // current height of picture
    private Picture pic; // current picture

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("Null picture.");
        pic = new Picture(picture);
        width = pic.width();
        height = pic.height();
    }


    // current picture
    public Picture picture() {
        return new Picture(pic);
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException("Coordinates out of range.");

        int x1 = pic.getRGB((x + width - 1) % width, y);
        int x2 = pic.getRGB((x + width + 1) % width, y);
        int y1 = pic.getRGB(x, (y + height - 1) % height);
        int y2 = pic.getRGB(x, (y + height + 1) % height);

        return Math.sqrt(energyHelper(x1, x2) + energyHelper(y1, y2));

    }

    // helper method for calculating gradient between a and b
    private double energyHelper(int rgbA, int rgbB) {
        int rA = (rgbA >> 16) & 0xFF;
        int gA = (rgbA >> 8) & 0xFF;
        int bA = rgbA & 0xFF;
        int rB = (rgbB >> 16) & 0xFF;
        int gB = (rgbB >> 8) & 0xFF;
        int bB = rgbB & 0xFF;

        double energy = Math.pow(rA - rB, 2);
        energy += Math.pow(gA - gB, 2);
        energy += Math.pow(bA - bB, 2);
        return energy;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] indices = verticalSeam();
        transpose();
        return indices;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return verticalSeam();
    }

    // helper method to find a vertical seam based on energies
    private int[] verticalSeam() {
        double[][] energy = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                energy[x][y] = energy(x, y);
            }
        }

        int w = width;
        int h = height;
        int[][] edgeTo = new int[w][h];
        double[][] distTo = new double[w][h];

        // loop through y first to capture the layout of Picture pixels
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                // simulate virtual top vertex with edges to first row vertices as -1
                if (y == 0) {
                    edgeTo[x][y] = -1;
                    distTo[x][y] = energy[x][y];
                    continue;
                }
                distTo[x][y] = energy[x][y];

                double x1 = Double.POSITIVE_INFINITY;
                if (x - 1 >= 0) x1 = distTo[x - 1][y - 1];
                double x2 = distTo[x][y - 1];
                double x3 = Double.POSITIVE_INFINITY;
                if (x + 1 < w) x3 = distTo[x + 1][y - 1];

                if (x1 <= x2 && x1 <= x3) {
                    distTo[x][y] += x1;
                    edgeTo[x][y] = x - 1;
                }
                else if (x2 <= x1 && x2 <= x3) {
                    distTo[x][y] += x2;
                    edgeTo[x][y] = x;
                }
                else {
                    distTo[x][y] += x3;
                    edgeTo[x][y] = x + 1;
                }
            }
        }
        double minEnergy = Double.POSITIVE_INFINITY;
        int minIndex = 0;
        for (int x = 0; x < w; x++) {
            if (distTo[x][h - 1] < minEnergy) {
                minEnergy = distTo[x][h - 1];
                minIndex = x;
            }
        }
        int[] indices = new int[h];
        for (int y = h - 1; y >= 0; y--) {
            indices[y] = minIndex;
            minIndex = edgeTo[minIndex][y];
        }
        return indices;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        transpose();
        removeSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        removeSeam(seam);
        width = pic.width();
        height = pic.height();
    }

    // helper method to transpose the Picture
    private void transpose() {
        Picture transposed = new Picture(pic.height(), pic.width());
        for (int i = 0; i < transposed.width(); i++) {
            for (int j = 0; j < transposed.height(); j++) {
                transposed.setRGB(i, j, pic.getRGB(j, i));
            }
        }
        pic = transposed;
        width = pic.width();
        height = pic.height();
    }

    // helper method to remove seam
    private void removeSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("Null seam argument.");
        width = pic.width();
        height = pic.height();
        if (seam.length != height) throw new IllegalArgumentException("Invalid seam.");
        if (width == 1)
            throw new IllegalArgumentException("Picture width/height is 1.");
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width)
                throw new IllegalArgumentException("Invalid seam indices.");
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException("Invalid seam indices.");
        }

        Picture newPic = new Picture(width - 1, height);
        for (int y = 0; y < height; y++) {
            int offset = 0;
            for (int x = 0; x < width; x++) {
                if (x == seam[y]) {
                    offset = 1;
                    continue;
                }
                newPic.setRGB(x - offset, y, pic.getRGB(x, y));
            }
        }
        pic = newPic;
    }

    //  unit testing (required)
    public static void main(String[] args) {
        SeamCarver sc = new SeamCarver(new Picture("HJoceanSmall.png"));
        int width = sc.width();
        int height = sc.height();
        System.out.println("Picture width: " + width + ", height: " + height);
        // original picture
        sc.picture().show();
        System.out.println();
        System.out.println("Energy at top-left: " + sc.energy(0, 0));
        try {
            System.out.println("Try getting energy outside of coordinate range.");
            System.out.println("Energy at bottom-right: " + sc.energy(width, height));
        }
        catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException caught.");
        }
        System.out.println("Energy at bottom-right: "
                                   + sc.energy(width - 1, height - 1));
        System.out.println("Energy in middle: " + sc.energy(width / 2, height / 2));
        System.out.println("Find and remove 100 vertical seams.");
        for (int i = 0; i < 100; i++) {
            int[] indices = sc.findVerticalSeam();
            System.out.println("Vertical Seam " + i + " " + Arrays.toString(indices));
            sc.removeVerticalSeam(indices);
        }
        System.out.println("Picture after removal: displayed in 2nd window");
        sc.picture().show();
        System.out.println("Find and remove 100 horizontal seams.");
        for (int i = 0; i < 100; i++) {
            int[] indices = sc.findHorizontalSeam();
            System.out.println("Horizontal Seam " + i + " "
                                       + Arrays.toString(indices));
            sc.removeHorizontalSeam(indices);
        }
        System.out.println("Picture after removal: displayed in 3rd window");
        sc.picture().show();

    }
}
