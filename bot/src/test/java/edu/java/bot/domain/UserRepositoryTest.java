package edu.java.bot.domain;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserRepositoryTest {
    private final Map<Long, User> userMapMock = Mockito.mock(Map.class);
    private final Map<Long, Set<URL>> userLinksMapMock = Mockito.mock(Map.class);
    private final UserRepository userRepository = new UserRepository(userMapMock, userLinksMapMock);

    static Stream<Arguments> provideDataForTest() throws URISyntaxException, MalformedURLException {
        Map<Long, User> usersMap = new HashMap<>();
        usersMap.put(1L, Mockito.mock(User.class));
        usersMap.put(2L, Mockito.mock(User.class));
        usersMap.put(300000L, Mockito.mock(User.class));
        Map<Long, Set<URL>> userLinksMap = new HashMap<>();
        userLinksMap.put(1L, new HashSet<>());
        userLinksMap.put(2L, new HashSet<>(Collections.singletonList(new URI("https://www.tinkoff.ru/").toURL())));
        userLinksMap.put(
            300000L,
            new HashSet<>(Arrays.asList(
                new URI("https://www.tinkoff.ru/").toURL(),
                new URI("https://edu.tinkoff.ru").toURL()
            ))
        );
        return Stream.of(Arguments.of(usersMap, userLinksMap));
    }

    @ParameterizedTest
    @MethodSource("provideDataForTest")
    public void testAddUser(Map<Long, User> users) {
        Map<Long, User> userMap = new HashMap<>();
        UserRepository userRepository = new UserRepository(userMap, userLinksMapMock);
        users.forEach((key, value) -> assertThat(userRepository.addUser(key, value)).isEqualTo(Optional.of(key)));
        assertThat(userMap.size()).isEqualTo(users.size());
    }

    @Test
    public void testAddKnownUser() {
        User user = new User(1L);
        when(userMapMock.putIfAbsent(any(), any())).thenReturn(user);
        assertThat(userRepository.addUser(1L, user)).isEqualTo(Optional.empty());
    }

    @ParameterizedTest
    @MethodSource("provideDataForTest")
    public void testRemoveUser(Map<Long, User> users, Map<Long, Set<URL>> userLinks) {
        Map<Long, User> userMap = new HashMap<>(users);
        Map<Long, Set<URL>> userLinksMap = new HashMap<>(userLinks);
        UserRepository userRepository = new UserRepository(userMap, userLinksMap);
        for (Long id : users.keySet()) {
            assertThat(userRepository.removeUser(id)).isEqualTo(Optional.of(userLinks.get(id)));
            assertThat(userMap).doesNotContainKey(id);
            assertThat(userLinksMap).doesNotContainKey(id);
        }
        assertThat(userMap).isEmpty();
        assertThat(userLinksMap).isEmpty();
    }

    @Test
    public void testRemoveUnknownUser() {
        when(userMapMock.remove(any())).thenReturn(null);
        assertThat(userRepository.removeUser(1L)).isEqualTo(Optional.empty());
    }

    @ParameterizedTest
    @MethodSource("provideDataForTest")
    public void testAddLinkUser(Map<Long, User> users, Map<Long, Set<URL>> userLinks) {
        Map<Long, Set<URL>> userLinksMap = new HashMap<>();
        UserRepository userRepository = new UserRepository(users, userLinksMap);
        for (Long id : userLinks.keySet()) {
            if (!userLinks.get(id).isEmpty()) {
                userLinks.get(id)
                    .forEach(value -> assertThat(userRepository.addLinkToUser(
                        id,
                        value
                    )).isEqualTo(Optional.of(value)));
                assertThat(userLinksMap.get(id)).isEqualTo(userLinks.get(id));
            }
        }
    }

    @Test
    public void testAddKnownLinkUser() throws URISyntaxException, MalformedURLException {
        URL url = new URI("https://www.tinkoff.ru/").toURL();
        when(userLinksMapMock.get(any())).thenReturn(Set.of(url));
        assertThat(userRepository.addLinkToUser(1L, url)).isEqualTo(Optional.empty());
    }

    @ParameterizedTest
    @MethodSource("provideDataForTest")
    public void testRemoveLinkFromUser(Map<Long, User> users, Map<Long, Set<URL>> userLinks) {
        UserRepository userRepository = new UserRepository(users, userLinks);
        for (Long id : users.keySet()) {
            Set<URL> links = new HashSet<>(userLinks.get(id));
            for (URL url : links) {
                assertThat(userRepository.removeLinkFromUser(id, url)).isEqualTo(Optional.of(url));
            }
            assertThat(userLinks.get(id)).isEmpty();
        }
    }

    @Test
    public void testRemoveUnknownLinkFromUser() throws URISyntaxException, MalformedURLException {
        URL url = new URI("https://www.tinkoff.ru/").toURL();
        when(userLinksMapMock.get(any())).thenReturn(new HashSet<>());
        assertThat(userRepository.removeLinkFromUser(1L, url)).isEqualTo(Optional.empty());
    }

    @ParameterizedTest
    @MethodSource("provideDataForTest")
    public void testGetUserLinks(Map<Long, User> users, Map<Long, Set<URL>> userLinks) {
        when(userMapMock.containsKey(any())).thenReturn(true);
        UserRepository userRepository = new UserRepository(userMapMock, userLinks);
        for (Long id : userLinks.keySet()) {
            assertThat(userRepository.getUserLinks(id)).isEqualTo(Optional.of(userLinks.get(id)));
        }
    }

    @Test
    public void testGetUnknownUserLinks() {
        when(userMapMock.containsKey(any())).thenReturn(false);
        assertThat(userRepository.getUserLinks(1L)).isEqualTo(Optional.empty());
    }

}

