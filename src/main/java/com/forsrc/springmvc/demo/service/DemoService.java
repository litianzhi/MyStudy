package com.forsrc.springmvc.demo.service;


import com.forsrc.springmvc.restful.base.service.RestfulService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * The interface Demo service.
 *
 * @param <E>  the type parameter
 * @param <PK> the type parameter
 */
@Service
@Transactional
public interface DemoService<E, PK extends Serializable>
        extends RestfulService<E, PK> {

}
