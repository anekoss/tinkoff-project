package edu.java.service;

import edu.java.domain.Link;
import java.util.List;

public interface LinkUpdaterService {
    List<Link> update();

    long sendUpdates(List<Link> links);
}
