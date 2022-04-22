import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { INotificationConfiguration } from 'app/shared/model/notification-configuration.model';
import { getEntity, updateEntity, createEntity, reset } from './notification-configuration.reducer';

export const NotificationConfigurationUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const notificationConfigurationEntity = useAppSelector(state => state.notificationConfiguration.entity);
  const loading = useAppSelector(state => state.notificationConfiguration.loading);
  const updating = useAppSelector(state => state.notificationConfiguration.updating);
  const updateSuccess = useAppSelector(state => state.notificationConfiguration.updateSuccess);
  const handleClose = () => {
    props.history.push('/notification-configuration');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...notificationConfigurationEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...notificationConfigurationEntity,
          user: notificationConfigurationEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2
            id="clapApplicationApp.notificationConfiguration.home.createOrEditLabel"
            data-cy="NotificationConfigurationCreateUpdateHeading"
          >
            <Translate contentKey="clapApplicationApp.notificationConfiguration.home.createOrEditLabel">
              Create or edit a NotificationConfiguration
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="notification-configuration-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('clapApplicationApp.notificationConfiguration.byComments')}
                id="notification-configuration-byComments"
                name="byComments"
                data-cy="byComments"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('clapApplicationApp.notificationConfiguration.byLikes')}
                id="notification-configuration-byLikes"
                name="byLikes"
                data-cy="byLikes"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('clapApplicationApp.notificationConfiguration.bySavings')}
                id="notification-configuration-bySavings"
                name="bySavings"
                data-cy="bySavings"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('clapApplicationApp.notificationConfiguration.bySubscriptions')}
                id="notification-configuration-bySubscriptions"
                name="bySubscriptions"
                data-cy="bySubscriptions"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('clapApplicationApp.notificationConfiguration.byPrivacyRequests')}
                id="notification-configuration-byPrivacyRequests"
                name="byPrivacyRequests"
                data-cy="byPrivacyRequests"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('clapApplicationApp.notificationConfiguration.byPrivacyRequestsStatus')}
                id="notification-configuration-byPrivacyRequestsStatus"
                name="byPrivacyRequestsStatus"
                data-cy="byPrivacyRequestsStatus"
                check
                type="checkbox"
              />
              <ValidatedField
                id="notification-configuration-user"
                name="user"
                data-cy="user"
                label={translate('clapApplicationApp.notificationConfiguration.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/notification-configuration" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default NotificationConfigurationUpdate;
