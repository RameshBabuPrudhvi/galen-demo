package org.example.tests;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.stream.IntStream;

public class ImageComparator {

    public static Optional<BufferedImage> diff(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            throw new IllegalArgumentException("Image dimensions mismatch");
        }

        int width = img1.getWidth();
        int height = img1.getHeight();
        var diffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        boolean[] hasDifference = {false};

        IntStream.range(0, height).parallel().forEach(y ->
                IntStream.range(0, width).forEach(x -> {

                    int rgb1 = img1.getRGB(x, y);
                    int rgb2 = img2.getRGB(x, y);
                    if (rgb1 == rgb2) {
                        diffImage.setRGB(x, y, rgb1);
                    } else {
                        diffImage.setRGB(x, y, Color.RED.getRGB());
                        hasDifference[0] = true;
                    }
                })
        );

        return hasDifference[0] ? Optional.of(diffImage) : Optional.empty();
    }
}
