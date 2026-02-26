INSERT INTO CUSTOM_USER (username, password) VALUES ('user','$2a$10$ZkZA2Dl3.AhzgEiG.grCPeE1pGstfhNARSFcs8oImpFABp5sNhC5e');
INSERT INTO CUSTOM_USER (username, password) VALUES ('monitoring','$2a$10$ZkZA2Dl3.AhzgEiG.grCPeE1pGstfhNARSFcs8oImpFABp5sNhC5e');

INSERT INTO ROLE (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO ROLE (id, name) VALUES (2, 'ROLE_ADMIN');
INSERT INTO ROLE (id, name) VALUES (3, 'ROLE_MONITORING');

INSERT INTO APP_USER_ROLES (app_user_id, role_id) VALUES (1, 1);
INSERT INTO APP_USER_ROLES (app_user_id, role_id) VALUES (2, 3);