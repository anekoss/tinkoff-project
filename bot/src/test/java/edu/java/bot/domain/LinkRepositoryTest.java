package edu.java.bot.domain;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

public class LinkRepositoryTest {

    private final Map<URL, Link> urlLinkMap = new HashMap<>();
    private final Map<URL, Set<Long>> linkUsersMap = new HashMap<>();
    private LinkRepository linkRepository = new LinkRepository(urlLinkMap, linkUsersMap);

    static Stream<Arguments> provideDataForTest() throws URISyntaxException, MalformedURLException {
        Map<URL, Link> urlLinkMap = new HashMap<>();
        URL url = new URI("https://www.tinkoff.ru/").toURL();
        URL eduUrl = new URI("https://edu.tinkoff.ru").toURL();
        urlLinkMap.put(url, Mockito.mock(Link.class));
        urlLinkMap.put(eduUrl, Mockito.mock(Link.class));
        Map<URL, Set<Long>> linkUsersMap = new HashMap<>();
        Set userSet = new HashSet();
        userSet.add(300000L);
        linkUsersMap.put(eduUrl, userSet);
        userSet = new HashSet();
        userSet.add(300000L);
        userSet.add(2L);
        linkUsersMap.put(url, userSet);
        return Stream.of(Arguments.of(
            urlLinkMap,
            linkUsersMap,
            Map.of(2L, Set.of(url), 300000L, Set.of(url, eduUrl), 1L, Set.of())
        ));
    }

    @Test
    public void testAddUserToUnknownLink() {
        URL urlMock = Mockito.mock(URL.class);
        Link linkMock = Mockito.mock(Link.class);
        linkRepository.addUserToLink(1L, urlMock, linkMock);
        assertThat(linkUsersMap.get(urlMock)).isEqualTo(Set.of(1L));
        assertThat(urlLinkMap.get(urlMock)).isEqualTo(linkMock);
    }

    @Test
    public void testAddUserToKnownLink() {
        URL urlMock = Mockito.mock(URL.class);
        Link linkMock = Mockito.mock(Link.class);
        urlLinkMap.put(urlMock, linkMock);
        Set<Long> users = new HashSet<>();
        users.add(1L);
        linkUsersMap.put(urlMock, users);
        linkRepository.addUserToLink(2L, urlMock, linkMock);
        assertThat(linkUsersMap.get(urlMock)).isEqualTo(Set.of(1L, 2L));
        assertThat(urlLinkMap.size()).isEqualTo(1);
    }

    @ParameterizedTest
    @MethodSource("provideDataForTest")
    void testRemoveUserFromKnownLink(Map<URL, Link> urlLinkMap, Map<URL, Set<Long>> linkUsersMap)
        throws URISyntaxException, MalformedURLException {
        linkRepository = new LinkRepository(urlLinkMap, linkUsersMap);
        URL url = new URI("https://www.tinkoff.ru/").toURL();
        linkRepository.removeUserFromLink(2L, url);
        assertThat(linkUsersMap.get(url)).isEqualTo(Set.of(300000L));
    }

    @ParameterizedTest
    @MethodSource("provideDataForTest")
    void testRemoveLastUserFromLink(Map<URL, Link> urlLinkMap, Map<URL, Set<Long>> linkUsersMap)
        throws URISyntaxException, MalformedURLException {
        linkRepository = new LinkRepository(urlLinkMap, linkUsersMap);
        URL eduUrl = new URI("https://edu.tinkoff.ru").toURL();
        linkRepository.removeUserFromLink(300000L, eduUrl);
        assertThat(linkUsersMap).doesNotContainKey(eduUrl);
    }

    @ParameterizedTest
    @MethodSource("provideDataForTest")
    void testRemoveUserFromLink(
        Map<URL, Link> urlLinkMap,
        Map<URL, Set<Long>> linkUsersMap,
        Map<Long, Set<URL>> userLinks
    ) {
        linkRepository = new LinkRepository(urlLinkMap, linkUsersMap);
        userLinks.forEach((key, value) -> linkRepository.removeUserFromLink(key, value));
        assertThat(urlLinkMap).isEmpty();
        assertThat(linkUsersMap).isEmpty();
    }

}
