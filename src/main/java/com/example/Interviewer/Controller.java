package com.example.Interviewer;

import com.example.Interviewer.Model.*;
import com.example.Interviewer.Model.ChatGPT.ChatRequest;
import com.example.Interviewer.Model.ChatGPT.ChatResponse;
import com.example.Interviewer.Model.ChatGPT.Quest;
import com.example.Interviewer.jwtFiles.JwtService;
import jakarta.validation.Valid;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private DbClassRepo repoInst;  // Instantiation of Feed Model Repo
    @Autowired
    private GPTRepo ChatGPTRepoInstance; // Instantiation of GPTLog Repo
    @Autowired
    private UserInfoService service;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;
    @PostMapping("/v1/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    @PostMapping("/v1/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
    @GetMapping("/v2/Questions")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<DbClass> getQuestions(){
        return repoInst.findAll();
    }

    @GetMapping("/v2/Questions/Prompt/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<GPTModel> getQuestionByIdForGPT(
            @PathVariable(value = "id") Long Id
    )throws ResourceNotFoundException{
        DbClass instance=repoInst.findById(Id).orElseThrow(()->new ResourceNotFoundException("Question Number Not Found::"+Id));
        GPTModel newChatModel= new GPTModel();
        newChatModel.setQuestion(instance.getQuestion());
        String prompt=chat(newChatModel.getQuestion());
        newChatModel.setPrompt(prompt);
        ChatGPTRepoInstance.save(newChatModel);
        return ResponseEntity.ok().body(newChatModel);
    }
    @PostMapping("/v2/Questions/Prompt")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<GPTModel> getQuestionForGPT( @RequestBody Quest authRequest){
        GPTModel newChatModel= new GPTModel();
        System.out.println(authRequest.getQues());
        String prompt=chat(authRequest.getQues());
        newChatModel.setPrompt(prompt);
        newChatModel.setQuestion(authRequest.getQues());
        ChatGPTRepoInstance.save(newChatModel);
        return ResponseEntity.ok().body(newChatModel);
    }

    public String chat(String prompt) {
        // create a request
        ChatRequest request = new ChatRequest(model, prompt);
        // call the API
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }
        //System.out.println(response.getChoices().getFirst().getMessage().getContent());
        System.out.println(response.getChoices().toString());
        //return response.getChoices().getFirst().getMessage().getContent();
        return response.getChoices().toString();
    }

    @GetMapping("/v2/Questions/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> getQuestionById(
            @PathVariable(value = "id") Long Id
    )throws ResourceNotFoundException{
        DbClass instance=repoInst.findById(Id).orElseThrow(()->new ResourceNotFoundException("Question Number Not Found::"+Id));
        return ResponseEntity.ok().body(instance.getQuestion());
    }

    @PostMapping("/v2/Questions")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public DbClass createQuestion(@Valid @RequestBody DbClass instance){
        return repoInst.save(instance);
    }

    @PutMapping("/v2/Questions/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<DbClass> updateQuestion(@PathVariable(value="id") Long Id,@Valid @RequestBody DbClass instance) throws ResourceNotFoundException{
        DbClass newVar=repoInst.findById(Id).orElseThrow(()->new ResourceNotFoundException("Question Not Found::"+Id));
        newVar.setQuestion(instance.getQuestion());
        final DbClass updatedInstance=repoInst.save(newVar);
        return ResponseEntity.ok(updatedInstance);
    }

    @DeleteMapping("/v2/Questions/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Map<String,Boolean> deleteQuestion(@PathVariable(value="id")Long Id)throws ResourceNotFoundException{
        DbClass newInstance=repoInst.findById(Id).orElseThrow(()->new ResourceNotFoundException("Question not Found::"+Id));
        repoInst.delete(newInstance);
        var response=new HashMap<String,Boolean>();
        response.put("Deleted",Boolean.TRUE);
        return response;
    }



}
