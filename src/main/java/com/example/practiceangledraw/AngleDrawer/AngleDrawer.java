package com.example.practiceangledraw.AngleDrawer;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Paint;

public class AngleDrawer extends Pane {
    private boolean selectionMode = false;


    private int clickCount = 0;
    private Point2D tempP1, tempP2;
    private Circle dummyP1Circle, dummyP2Circle;
    private Line dummyLine1;




    private final List<AngleSet> angleSets = new ArrayList<>();

    private final ImageView imageView;

    public AngleDrawer(ImageView imageView) {
        this.imageView = imageView;
        setPrefSize(1080, 1080);
        setStyle("-fx-background-color: transparent;");
        setOnMouseClicked(this::handleClick);
    }


    private boolean isClickInsideActualImage(MouseEvent event) {
        Image img = imageView.getImage();
        if (img == null) return false;

        double imageWidth = img.getWidth();
        double imageHeight = img.getHeight();

        double fitWidth = imageView.getFitWidth();
        double fitHeight = imageView.getFitHeight();

        double ratioX = fitWidth / imageWidth;
        double ratioY = fitHeight / imageHeight;
        double ratio = Math.min(ratioX, ratioY);

        double displayedWidth = imageWidth * ratio;
        double displayedHeight = imageHeight * ratio;

        double offsetX = (fitWidth - displayedWidth) / 2;
        double offsetY = (fitHeight - displayedHeight) / 2;

        double clickX = event.getX();
        double clickY = event.getY();

        // Check if click is inside the actual image area
        return clickX >= offsetX && clickX <= (offsetX + displayedWidth)
                && clickY >= offsetY && clickY <= (offsetY + displayedHeight);
    }





    //==========================================================================
//    handle click




    private void handleClick(MouseEvent event) {

        if (selectionMode) return;

        if (!isClickInsideActualImage(event)) return;

        double x = event.getX();
        double y = event.getY();
        Point2D clicked = new Point2D(x, y);


        for (AngleSet set : angleSets) {
            if (set.isTooClose(clicked)) return;
        }

        if (clickCount % 3 == 0) {
            tempP1 = clicked;
            dummyP1Circle = createDummyCircle(tempP1);
            getChildren().add(dummyP1Circle);
        } else if (clickCount % 3 == 1) {

            tempP2 = clicked;
            dummyP2Circle = createDummyCircle(tempP2);
            dummyLine1 = new Line(tempP1.getX(), tempP1.getY(), tempP2.getX(), tempP2.getY());
            dummyLine1.setStroke(Color.web("#EC641C"));
            dummyLine1.setStrokeWidth(3);
            getChildren().addAll(dummyP2Circle, dummyLine1);
        } else if (clickCount % 3 == 2) {
            Point2D tempP3 = clicked;
            getChildren().removeAll(dummyP1Circle, dummyP2Circle, dummyLine1);
            AngleSet set = new AngleSet(tempP1, tempP2, tempP3);
            angleSets.add(set);
            getChildren().addAll(
                    set.p1Circle,
                    set.p2Circle,
                    set.p3Circle,
                    set.line1,
                    set.line2,
                    set.angleText
            );

        }

        clickCount++;
    }


    private Circle createDummyCircle(Point2D point) {
        Circle circle = new Circle(point.getX(), point.getY(), 8, Color.web("#EC641C"));
        circle.setCursor(Cursor.HAND);
        return circle;
    }

    // ------------------- AngleSet Class -------------------
    private class AngleSet {
        Point2D p1, p2, p3;
        Circle p1Circle, p2Circle, p3Circle;
        Line line1, line2;
        Text angleText;
        boolean selected = false;

        AngleSet(Point2D p1, Point2D p2, Point2D p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;

            p1Circle = createDraggableCircle(p1, 1);
            p2Circle = createDraggableCircle(p2, 2);
            p3Circle = createDraggableCircle(p3, 3);

            line1 = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            line2 = new Line(p2.getX(), p2.getY(), p3.getX(), p3.getY());
            line1.setStroke(Color.web("#EC641C"));
            line2.setStroke(Color.web("#EC641C"));
            line1.setStrokeWidth(3);
            line2.setStrokeWidth(3);

            double angle = calculateAngle(p1, p2, p3);
            angleText = new Text(p2.getX() + 10, p2.getY() - 10, String.format("%.2f°", angle));
            angleText.setFont(Font.font(16));
            angleText.setFill(Color.web("#EC641C"));



            registerClickHandlers(); // 👈 Important for selection
        }

        public void setSelected(boolean selected) {
            this.selected = selected;

            Color color = selected ? Color.LIGHTBLUE : Color.web("#EC641C");
            double strokeWidth = selected ? 5.0 : 3.0;

            p1Circle.setFill(color);
            p2Circle.setFill(color);
            p3Circle.setFill(color);

            line1.setStroke(color);
            line1.setStrokeWidth(strokeWidth);

            line2.setStroke(color);
            line2.setStrokeWidth(strokeWidth);

            angleText.setFill(color);
        }


        private void registerClickHandlers() {
            EventHandler<MouseEvent> handler = e -> {
                if (!selectionMode) return;

                if (e.isControlDown()) {
                    // Toggle selection
                    setSelected(!selected);
                } else {
                    // Deselect all others
                    for (AngleSet other : angleSets) {
                        other.setSelected(false);
                    }
                    setSelected(true);
                }

                e.consume();
            };

            // Add handler to all parts
            p1Circle.setOnMouseClicked(handler);
            p2Circle.setOnMouseClicked(handler);
            p3Circle.setOnMouseClicked(handler);
            line1.setOnMouseClicked(handler);
            line2.setOnMouseClicked(handler);
            angleText.setOnMouseClicked(handler);
        }

        public boolean contains(Node node) {
            return node == p1Circle || node == p2Circle || node == p3Circle ||
                    node == line1 || node == line2 || node == angleText;
        }

        private Circle createDraggableCircle(Point2D point, int index) {
            Circle circle = new Circle(point.getX(), point.getY(), 8, Color.web("#EC641C"));
            circle.setCursor(Cursor.HAND);

            final boolean[] canDrag = {false};
            final double[] offset = new double[2];

            circle.setOnMousePressed(e -> {
                double dx = e.getX() - circle.getCenterX();
                double dy = e.getY() - circle.getCenterY();
                double distance = Math.sqrt(dx * dx + dy * dy);

                canDrag[0] = distance <= circle.getRadius() + 4.0;
                if (canDrag[0]) {
                    offset[0] = dx;
                    offset[1] = dy;
                }
            });

            circle.setOnMouseDragged(e -> {
                if (!canDrag[0]) return;

                double imgX = imageView.getLayoutX();
                double imgY = imageView.getLayoutY();
                double imgW = imageView.getFitWidth();
                double imgH = imageView.getFitHeight();

                double x = Math.max(imgX, Math.min(imgX + imgW, e.getX() - offset[0]));
                double y = Math.max(imgY, Math.min(imgY + imgH, e.getY() - offset[1]));

                circle.setCenterX(x);
                circle.setCenterY(y);

                if (index == 1) {
                    p1 = new Point2D(x, y);
                    line1.setStartX(x);
                    line1.setStartY(y);
                } else if (index == 2) {
                    p2 = new Point2D(x, y);
                    line1.setEndX(x);
                    line1.setEndY(y);
                    line2.setStartX(x);
                    line2.setStartY(y);
                    angleText.setX(x + 10);
                    angleText.setY(y - 10);
                } else if (index == 3) {
                    p3 = new Point2D(x, y);
                    line2.setEndX(x);
                    line2.setEndY(y);
                }

                if (p1 != null && p2 != null && p3 != null) {
                    double angle = calculateAngle(p1, p2, p3);
                    angleText.setText(String.format("%.2f°", angle));
                }
            });

            return circle;
        }

        boolean isTooClose(Point2D point) {
            double threshold = 8.0;
            return (p1.distance(point) < threshold ||
                    p2.distance(point) < threshold ||
                    p3.distance(point) < threshold);
        }
    }





    private double calculateAngle(Point2D a, Point2D vertex, Point2D b) {
        Point2D v1 = a.subtract(vertex);
        Point2D v2 = b.subtract(vertex);
        double dot = v1.dotProduct(v2);
        double mag1 = v1.magnitude();
        double mag2 = v2.magnitude();
        if (mag1 == 0 || mag2 == 0) return 0;
        double cosTheta = dot / (mag1 * mag2);
        cosTheta = Math.max(-1, Math.min(1, cosTheta)); // Clamp
        return Math.toDegrees(Math.acos(cosTheta));
    }

    public void hideAll() {
        for (Node node : getChildren()) {
            node.setVisible(false);
        }

    }



    public void showAll() {
        for (Node node : getChildren()) {
            node.setVisible(true);
        }

    }


    public boolean isCurrentlyHidden() {
        // Check any one node to determine if hidden
        return getChildren().stream().anyMatch(n -> !n.isVisible());
    }

    public void clearAll() {
        getChildren().clear();       // remove all UI nodes from canvas
        angleSets.clear();           // remove stored sets
        clickCount = 0;              // reset click cycle
    }

    public void setSelectionMode(boolean value) {
        this.selectionMode = value;
    }
    public void deselectAll() {
        for (AngleSet set : angleSets) {
            set.setSelected(false);
        }
    }

    public void deleteSelectedAngles() {
        List<AngleSet> toRemove = new ArrayList<>();
        for (AngleSet set : angleSets) {
            if (set.selected) {
                getChildren().removeAll(
                        set.p1Circle, set.p2Circle, set.p3Circle,
                        set.line1, set.line2, set.angleText
                );
                toRemove.add(set);
            }
        }
        angleSets.removeAll(toRemove);
    }

}

