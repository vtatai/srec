package com.github.srec.rec.common;

import java.awt.*;

/**
 * Matches components based on criteria.
 *
 * @author Vivek Prahlad
 */
public interface ComponentMatchingRule {
    public boolean matchAndContinue(Component component);

    public boolean hasMatches();
}
