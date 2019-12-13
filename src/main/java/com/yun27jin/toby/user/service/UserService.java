package com.yun27jin.toby.user.service;

import com.yun27jin.toby.user.dao.UserDao;
import com.yun27jin.toby.user.domain.User;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Properties;

public class UserService {
    private UserDao userDao;
    private MailSender mailSender;
    private PlatformTransactionManager transactionManager;

    public UserService setUserDao(UserDao userDao) {
        this.userDao = userDao;
        return this;
    }

    public UserService setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        return this;
    }

    public UserService setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
        return this;
    }

    public void upgradeLevels() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = this.userDao.get();
            for (User user : users) {
                if (user.canUpgradeLevel(user))
                    this.upgradeLevel(user);
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEMail(user);
    }

    private void sendUpgradeEMail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("yun27jin@gmail.com");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name());

        mailSender.send(mailMessage);
    }

    public static class TestUserService extends UserService {
        private String id;

        TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id))
                throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    public static class TestUserServiceException extends RuntimeException {
    }

}