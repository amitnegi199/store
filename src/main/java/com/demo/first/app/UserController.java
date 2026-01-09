package com.demo.first.app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private Map<Integer, User> userDb = new HashMap<>();

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userDb.putIfAbsent(user.getId(), user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        if (!userDb.containsKey(user.getId()))
            return ResponseEntity.notFound().build();
        userDb.put(user.getId(), user);
        return ResponseEntity.ok(user);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        if (!userDb.containsKey(id))
            return ResponseEntity.notFound().build();
        userDb.remove(id);
        return ResponseEntity.ok("user deleted Successfully");
    }

    @GetMapping()
    public List<User> getUser() {
        return new ArrayList<>(userDb.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id)
    {
        if (!userDb.containsKey(id))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userDb.get(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam(required=false,defaultValue="alice") String name , @RequestParam(required=false,defaultValue="email") String email)
    {
        System.out.println(name);
        List<User> users=userDb.values().stream().filter(u->u.getName().equalsIgnoreCase(name)).filter(u->u.getEmail().equalsIgnoreCase(email)).toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/info/{id}")

    public String getInfo(
            @PathVariable int id,
            @RequestParam String name,
            @RequestHeader("User-Agent") String userAgent){
        return "User-Agent:" + userAgent + " : " + id + " : " + name;
    }
}
