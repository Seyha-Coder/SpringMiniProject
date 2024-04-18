package org.example.springminiproject.Service.OptService;

import org.example.springminiproject.Model.OPT.OptsDTO;

import java.util.Optional;

public interface OptService {
    Optional<OptsDTO> findById(Integer id);

    void save(OptsDTO optsDTO);
    Optional<OptsDTO> findByCode(String code);
    void uploadOpt(String optCode);
}
