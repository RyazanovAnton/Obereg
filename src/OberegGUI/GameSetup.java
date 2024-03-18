package OberegGUI;

import OberegEngine.Player.Player;
import OberegEngine.Player.Alliance;
import OberegGUI.MyGameTable.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class GameSetup extends JDialog {

    private PlayerType slavPlayerType;
    private PlayerType vikingPlayerType;
    private JSpinner searchDepthSpinner;

    private static final String HUMAN_TEXT = "Human";
    private static final String COMPUTER_TEXT = "Computer";

    GameSetup(final JFrame frame,
              final boolean modal) {
        super(frame, modal);
        final JPanel myPanel = new JPanel(new GridLayout(0, 1));
        final JRadioButton slavHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton slavComputerButton = new JRadioButton(COMPUTER_TEXT);
        final JRadioButton vikingHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton vikingComputerButton = new JRadioButton(COMPUTER_TEXT);
        slavHumanButton.setActionCommand(HUMAN_TEXT);
        final ButtonGroup whiteGroup = new ButtonGroup();
        whiteGroup.add(slavHumanButton);
        whiteGroup.add(slavComputerButton);
        slavHumanButton.setSelected(true);

        final ButtonGroup blackGroup = new ButtonGroup();
        blackGroup.add(vikingHumanButton);
        blackGroup.add(vikingComputerButton);
        vikingComputerButton.setSelected(true);

        getContentPane().add(myPanel);
        myPanel.add(new JLabel("Slavs"));
        myPanel.add(slavHumanButton);
        myPanel.add(slavComputerButton);
        myPanel.add(new JLabel("Vikings"));
        myPanel.add(vikingHumanButton);
        myPanel.add(vikingComputerButton);

        myPanel.add(new JLabel("Search"));
        this.searchDepthSpinner = addLabeledSpinner(myPanel, "Search Depth", new SpinnerNumberModel(4, 0, 10, 1));

        final JButton cancelButton = new JButton("Cancel");
        final JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                slavPlayerType = slavComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                vikingPlayerType = vikingComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                GameSetup.this.setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cancel");
                GameSetup.this.setVisible(false);
            }
        });

        myPanel.add(cancelButton);
        myPanel.add(okButton);

        setLocationRelativeTo(frame);
        pack();
        setVisible(false);
    }

    void promptUser() {
        setVisible(true);
        repaint();
    }

    boolean isAIPlayer(final Player player) {
        if(player.getAlliance() == Alliance.SLAVS) {
            return getSlavPlayerType() == PlayerType.COMPUTER;
        }
        return getVikingPlayerType() == PlayerType.COMPUTER;
    }

    PlayerType getSlavPlayerType() {
        return this.slavPlayerType;
    }

    PlayerType getVikingPlayerType() {
        return this.vikingPlayerType;
    }

    private static JSpinner addLabeledSpinner(final Container c,
                                              final String label,
                                              final SpinnerModel model) {
        final JLabel l = new JLabel(label);
        c.add(l);
        final JSpinner spinner = new JSpinner(model);
        l.setLabelFor(spinner);
        c.add(spinner);
        return spinner;
    }

    int getSearchDepth() {
        return (Integer)this.searchDepthSpinner.getValue();
    }
}
