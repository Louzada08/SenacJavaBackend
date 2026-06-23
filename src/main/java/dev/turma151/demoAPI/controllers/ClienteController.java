package dev.turma151.demoAPI.controllers;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.turma151.demoAPI.models.ClienteModel;
import dev.turma151.demoAPI.models.CountryModel;
import dev.turma151.demoAPI.repositories.ClienteRepository;
import dev.turma151.demoAPI.repositories.CountryRepository;

@RequestMapping("/api/v1")
@RestController
public class ClienteController {

    final ClienteRepository clienteRepository;
    final CountryRepository countryRepository;

    public ClienteController(ClienteRepository clienteRepository, CountryRepository countryRepository) {
            this.clienteRepository = clienteRepository;
            this.countryRepository = countryRepository;
    }
	

	@CrossOrigin(origins = "*")
    @GetMapping("/clientes")
	public ResponseEntity<List<ClienteModel>> getAllClientes() {
		List<ClienteModel> clientesList = clienteRepository.findAll();
        
        if(clientesList.isEmpty()) 
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        
        clientesList.forEach(cliente -> 
          cliente.add(linkTo(methodOn(ClienteController.class).getOneCliente(cliente.getId())).withSelfRel())); 
    
		return ResponseEntity.status(HttpStatus.OK).body(clientesList);
	}

	@GetMapping("/clientes/{id}")
	public ResponseEntity<Object> getOneCliente(@PathVariable(value="id") Integer id){
		return clienteRepository.findById(id)
				.map(cliente -> {
					cliente.add(linkTo(methodOn(ClienteController.class).getAllClientes()).withRel("Clientes List"));
					return ResponseEntity.status(HttpStatus.OK).body((Object) cliente);
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente not found."));
	}   
	
@CrossOrigin(origins = "*")
@PostMapping("/clientes")
public ResponseEntity<?> saveCliente(@RequestBody ClienteModel cliente) {

    // valida se foi enviado o country
    if (cliente.getCountry() == null || cliente.getCountry().getCode() == null) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Country code is required");
    }

    // busca o country no banco
    String code = cliente.getCountry().getCode();

    CountryModel country = countryRepository.findById(code)
            .orElseThrow(() -> new RuntimeException("Country not found"));

    // seta o country válido
    cliente.setCountry(country);

    // salva no banco
    ClienteModel savedCliente = clienteRepository.save(cliente);

    // adiciona HATEOAS
    savedCliente.add(
        linkTo(methodOn(ClienteController.class)
        .getOneCliente(savedCliente.getId())).withSelfRel()
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(savedCliente);
}
	
}
