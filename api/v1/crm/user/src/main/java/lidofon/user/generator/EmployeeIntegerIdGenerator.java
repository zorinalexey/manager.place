package lidofon.user.generator;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.hibernate.type.descriptor.java.spi.JavaTypeBasicAdaptor;
import org.hibernate.type.descriptor.jdbc.NumericJdbcType;
import org.hibernate.type.internal.NamedBasicTypeImpl;

import java.util.Properties;

@Slf4j
public class EmployeeIntegerIdGenerator extends SequenceStyleGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        log.debug("Generate employee integer id");
        Long maxIntId = sharedSessionContractImplementor.createQuery("SELECT COALESCE(MAX(e.intId), 0) FROM Employee e", Long.class)
                .getSingleResult();
        if (maxIntId == null) {
            maxIntId = 1L;
        } else {
            maxIntId = maxIntId + 1;
        }
        return maxIntId;
    }

    @Override
    public void configure(Type type, Properties parameters, ServiceRegistry serviceRegistry) throws MappingException {
        parameters.put(OptimizableGenerator.INCREMENT_PARAM, OptimizableGenerator.DEFAULT_INCREMENT_SIZE);
        Type idType = new NamedBasicTypeImpl<>(new JavaTypeBasicAdaptor<>(Long.class), NumericJdbcType.INSTANCE, "long");
        super.configure(idType, parameters, serviceRegistry);
    }
}
