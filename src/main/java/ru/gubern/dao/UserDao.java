package ru.gubern.dao;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import ru.gubern.dto.CompanyDto;
import ru.gubern.entity.*;

import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {

    @Getter
    private static final UserDao INSTANCE = new UserDao();

    public List<com.querydsl.core.Tuple> findAll(Session session) {
        /*return session.createQuery("select u from User u", User.class)
                .list();*/
        /*var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);

        criteria.select(user);*/

            return new JPAQuery<User>(session)
                    .select(QUser.user)
                    .from(QUser.user)
                    .fetch();
        }

    public List<User> findAllByFirstName(Session session, String firstName){
        /*return session.createQuery("select u from User u " +
                        "where u.personalInfo.firstname = :firstName", User.class)
                .setParameter("firstName", firstName)
                .list();*/

        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);

        criteria.select(user).where(
                cb.equal(user.get(User_.personInfo).get(PersonInfo_.firstname), firstName)
        );

        return session.createQuery(criteria).list();
    }

    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit){
        /*return session.createQuery("select u from User u " +
                        "order by u.personalInfo.firstname", User.class)
                .setMaxResults(limit)
                .list();*/
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);

        var user = criteria.from(User.class);

        criteria.select(user).orderBy(cb.asc(user.get(User_.personInfo)));
        return session.createQuery(criteria)
                .setMaxResults(limit)
                .list();
    }

    public List<User> findAllByCompanyName(Session session, String companyName) {
        /*return session.createQuery("select u from Company c " +
                "inner join c.users u " +
                "where c.companyName = :companyName", User.class)
                .setParameter("companyName", companyName)
                .list();*/

        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var company = criteria.from(Company.class);
        var users = company.join(Company_.users);

        criteria.select(users).where(
                cb.equal(company.get(Company_.companyName), companyName)
        );

        return session.createQuery(criteria)
                .list();
    }

    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        /*return session.createQuery("select p from Payment p " +
                        "join p.receiver u " +
                        "join u.company c " +
                        "where c.companyName = :companyName " +
                        "order by u.personalInfo.firstname asc, p.amount", Payment.class)
                .setParameter("companyName", companyName)
                .list();*/
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Payment.class);
        var payment = criteria.from(Payment.class);
        var users = payment.join(Payment_.receiver);
        var company = users.join(User_.company);

        criteria.select(payment).where(
                cb.equal(company.get(Company_.companyName), companyName)
        )
                .orderBy(
                        cb.asc(users.get(User_.personInfo).get(PersonInfo_.firstname)),
                        cb.asc(payment.get(Payment_.amount))
                );
        return session.createQuery(criteria)
                .list();
    }


    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, String firstName, String lastName){
        /*return session.createQuery("select avg (p.amount) from Payment p " +
                        "join p.receiver u " +
                        "where u.personInfo.firstname = :firstName " +
                        "and u.personInfo.lastname = :lastName", Double.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .uniqueResult();*/
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Double.class);

        var payment = criteria.from(Payment.class);
        var user = payment.join(Payment_.receiver);

        List<Predicate> predicates = new ArrayList<>();

        if (firstName != null){
            predicates.add(cb.equal(user.get(User_.personInfo).get(PersonInfo_.firstname), firstName));
        }

        if (lastName != null){
            predicates.add(cb.equal(user.get(User_.personInfo).get(PersonInfo_.lastname), lastName));
        }

        criteria.select(cb.avg(payment.get(Payment_.amount))).where(
          predicates.toArray(Predicate[]::new)
        );

        return session.createQuery(criteria)
                .uniqueResult();
    }

    public List<CompanyDto> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session){
        /*return session.createQuery("select c.companyName, avg(p.amount) from Company c " +
                        "join c.users u " +
                        "join u.payments p " +
                        "group by c.companyName " +
                        "order by c.companyName", Object[].class)
                .list();*/

        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(CompanyDto.class);

        var company = criteria.from(Company.class);
        var users = company.join(Company_.users);
        var payments = users.join(User_.payments);



        criteria.select(
                cb.construct(CompanyDto.class,
                        company.get(Company_.companyName),
                        cb.avg(payments.get(Payment_.amount))
                        )
        )
                .groupBy(company.get(Company_.companyName))
                .orderBy(cb.asc(company.get(Company_.companyName)));

        return session.createQuery(criteria)
                .list();
    }

    public List<Tuple> isItPossible(Session session){
        /*return session.createQuery("select u, avg(p.amount) from User u " +
                "join u.payments p" +
                " group by u " +
                "having avg (p.amount) > (select avg (p.amount) from  Payment p " +
                "order by u.personalInfo.firstname)",Object[].class).list();*/

        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Tuple.class);
        var users = criteria.from(User.class);
        var payment = users.join(User_.payments);

        var subquery = criteria.subquery(Double.class);

        var paymentSubquery = subquery.from(Payment.class);

        criteria.select(
                cb.tuple(
                        users,
                        cb.avg(payment.get(Payment_.amount))
                )
        ).groupBy(users.get(User_.id))
                .having(cb.gt(cb.avg(payment.get(Payment_.amount)),
                        subquery.select(cb.avg(paymentSubquery.get(Payment_.amount)))
                        )).orderBy(cb.asc(users.get(User_.personInfo).get(PersonInfo_.firstname)));

        return session.createQuery(criteria)
                .list();
    }
}
