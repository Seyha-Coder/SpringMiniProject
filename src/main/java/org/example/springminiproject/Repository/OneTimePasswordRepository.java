package org.example.springminiproject.Repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.springminiproject.Model.OPT.OptsDTO;

import java.util.Optional;

@Mapper
public interface OneTimePasswordRepository {

    Optional<OptsDTO> findById(Integer id);


    @Select("""
            INSERT INTO otps (opt_code, expiration,verified,user_id) VALUES(#{opt.optCode},#{opt.expiration},false,#{opt.userId})
            """)
    void createNewOpt(@Param("opt") OptsDTO optsDTO);

    @Select("""
            SELECT * FROM otps WHERE opt_code = #{code}
            """)
    OptsDTO findByCode(String code);

    @Update("""
            UPDATE otps SET verified = true WHERE opt_code = #{code}
            """)
    void save(String code);
}
