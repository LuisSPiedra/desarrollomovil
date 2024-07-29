package com.univerM.store.controller;

import com.univerM.store.entity.ProductoEntity;
import com.univerM.store.model.request.ProductoRequest;
import com.univerM.store.model.response.Notificacion;
import com.univerM.store.model.response.ProductoResponse;
import com.univerM.store.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductosController {

    @Autowired
    private ProductoRepository repository;

  @GetMapping("/producto/{id}")
    public ResponseEntity productos(@PathVariable Integer id){


      Optional<ProductoEntity> entity = this.repository.findById(id);
      if (entity.isPresent()){
          ProductoResponse response = new ProductoResponse();
          response.setName(entity.get().getNombre());
          response.setSku(entity.get().getSku());
          response.setCantidad(entity.get().getCantidad());
          return new ResponseEntity(response, HttpStatus.OK);
      }else{
          return new ResponseEntity("No hay informacion ligada al ID",HttpStatus.NO_CONTENT);
      }
      //return this.repository.findById(id).get();
        //return Arrays.asList("tornillos", "clavos","pegamento");

    }
    @CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
    @PostMapping("/save/product")
    public ResponseEntity save(@RequestBody ProductoRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        //respondeHeaders.setAccessControlAllowOrigin("http://localhost:5173");

        ProductoEntity entity=new ProductoEntity();
        entity.setNombre(request.getName());
        entity.setSku(request.getSku());
        entity.setCantidad(request.getCantidad());

        ProductoResponse response = new ProductoResponse();
        response.setName(entity.getNombre());

        Notificacion notificacion = new Notificacion();
        notificacion.setLevel("Success");
        notificacion.setReason("Exitoso");
        notificacion.setMessage("El producto: "+ request.getName() + " se registro de la manera correcta");
        response.setNotificacion(notificacion);

        try {
            repository.save(entity);
        }catch (DataAccessException e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(responseHeaders).body(response.getNotificacion());
        }

        return  ResponseEntity.status(HttpStatus.CREATED).
                headers(responseHeaders).
                body(response);
        //this.repository.save(entity);
        //return ResponseEntity.ok("El registro de realizo de manera correcto");
    }


}
