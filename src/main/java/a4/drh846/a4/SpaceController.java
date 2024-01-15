//DRH846
//11280305
//CMPT 381

package a4.drh846.a4;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.util.ArrayList;

public class SpaceController {
    private SpaceModel model;
    private InteractionModel iModel;
    private boolean movementBoxChecked = true;
    private boolean spinBoxChecked = true;

    private enum InteractionState {READY, DRAGGING}
    private InteractionState state = InteractionState.READY;

    public SpaceController() {

    }
    public void setModel(SpaceModel m) {
        model = m;
    }

    public void setIModel(InteractionModel iM) {
        iModel = iM;
    }

    public void handleAnimationTick() {
        if (movementBoxChecked) {
            model.moveAsteroids();
        }
        if (spinBoxChecked) {
            model.spinAsteroids();
        }
        // update world rotation
        iModel.rotateWorld(iModel.getWorldRotationSpeed());

        // collision with cursor to turn asteroid yellow
        if (state != InteractionState.DRAGGING) {
            Asteroid aHit = model.contains(iModel.get_mX(), iModel.get_mY());
            if (aHit != null) {
                if (iModel.getHovered().contains(aHit)) {
                    iModel.clearHovered();
                }
                iModel.addHovered(aHit);
            } else {
                iModel.clearHovered();
            }

            // collision with area cursor to turn asteroid yellow
            ArrayList<Asteroid> allHit = model.areaContains(iModel.get_mX(), iModel.get_mY(), iModel.getCursorSize());
            if (allHit != null) {
                for (Asteroid a : allHit) {
                    if (allHit.contains(a) && !iModel.getHovered().contains(a))
                        iModel.addHovered(a);
                    else if (!allHit.contains(a) && iModel.getHovered().contains(a))
                        iModel.removeHovered(a);
                }
            }
        }
    }

    // change world rotation based on slider value
    public void handleSliderDragged(double value) {
        iModel.setWorldRotationSpeed(value);
    }

    // toggle movement
    public void handleMovementBoxChecked() {
        movementBoxChecked = !movementBoxChecked;
    }

    // toggle asteroid spin
    public void handleSpinBoxChecked() {
        spinBoxChecked = !spinBoxChecked;
    }

    // update move position every time the mouse moves
    public void handleMouseMoved(MouseEvent e, double size) {
        double mx = e.getX();
        double my = e.getY();
        iModel.updateMousePos(mx, my, size);
    }

    // logic for selection when the mouse is pressed
    public void handleMousePressed(MouseEvent e) {
        switch (state) {
            case READY -> {
                // collision with cursor
                Asteroid aHit = model.contains(iModel.get_mX(), iModel.get_mY());
                if (aHit != null && !iModel.getSelected().contains(aHit)) {
                    iModel.addSelected(aHit);
                    aHit.setZ(model.getZ());
                    aHit.set_vX(0.0);
                    aHit.set_vY(0.0);
                }
                else {
                    iModel.clearSelected();
                }

                // collision with area cursor
                ArrayList<Asteroid> allHit = model.areaContains(iModel.get_mX(), iModel.get_mY(), iModel.getCursorSize());
                if (allHit != null) {
                    for (Asteroid a : allHit) {
                        if (!iModel.getSelected().contains(a)) {
                            iModel.addSelected(a);
                            a.set_vX(0.0);
                            a.set_vY(0.0);
                        }
                    }
                }// only clear selection if single pointer collision hit is null
                else if (aHit == null){
                    iModel.clearSelected();
                }

                state = InteractionState.DRAGGING;
            }
        }
    }

    // logic for moving asteroids in accordance with the mouse pos
    public void handleMouseDragging(MouseEvent e, double size) {
        switch (state) {
            case DRAGGING -> {
                iModel.updateMousePos(e.getX(), e.getY(), size);
                for (Asteroid a : iModel.getSelected()) {
                    a.set_tX(a.get_tX()+iModel.getDeltaX());
                    a.set_tY(a.get_tY()+iModel.getDeltaY());
                }
            }
        }
    }

    // give the cursors velocity to the selected asteroids on mouse release
    public void handleMouseReleased(MouseEvent e) {
        switch (state) {
            case DRAGGING -> {
                for (Asteroid a : iModel.getSelected()) {
                    a.set_vX(iModel.getDeltaX());
                    a.set_vY(iModel.getDeltaY());
                }
                iModel.clearSelected();
                state = InteractionState.READY;
            }
        }
    }

    // change size of area cursor
    public void handleWheel(ScrollEvent e) {
        iModel.changeCursorSize(e.getDeltaY());
    }

}

