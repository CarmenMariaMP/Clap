import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICompany } from 'app/shared/model/company.model';
import { getEntities as getCompanies } from 'app/entities/company/company.reducer';
import { IContentCreator } from 'app/shared/model/content-creator.model';
import { getEntities as getContentCreators } from 'app/entities/content-creator/content-creator.reducer';
import { IPrivacyRequest } from 'app/shared/model/privacy-request.model';
import { RequestStateType } from 'app/shared/model/enumerations/request-state-type.model';
import { getEntity, updateEntity, createEntity, reset } from './privacy-request.reducer';

export const PrivacyRequestUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const companies = useAppSelector(state => state.company.entities);
  const contentCreators = useAppSelector(state => state.contentCreator.entities);
  const privacyRequestEntity = useAppSelector(state => state.privacyRequest.entity);
  const loading = useAppSelector(state => state.privacyRequest.loading);
  const updating = useAppSelector(state => state.privacyRequest.updating);
  const updateSuccess = useAppSelector(state => state.privacyRequest.updateSuccess);
  const requestStateTypeValues = Object.keys(RequestStateType);
  const handleClose = () => {
    props.history.push('/privacy-request');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCompanies({}));
    dispatch(getContentCreators({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...privacyRequestEntity,
      ...values,
      company: companies.find(it => it.id.toString() === values.company.toString()),
      contentCreatto: contentCreators.find(it => it.id.toString() === values.contentCreatto.toString()),
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
          requestState: 'PENDING',
          ...privacyRequestEntity,
          company: privacyRequestEntity?.company?.id,
          contentCreatto: privacyRequestEntity?.contentCreatto?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="clapApplicationApp.privacyRequest.home.createOrEditLabel" data-cy="PrivacyRequestCreateUpdateHeading">
            <Translate contentKey="clapApplicationApp.privacyRequest.home.createOrEditLabel">Create or edit a PrivacyRequest</Translate>
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
                  id="privacy-request-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('clapApplicationApp.privacyRequest.requestState')}
                id="privacy-request-requestState"
                name="requestState"
                data-cy="requestState"
                type="select"
              >
                {requestStateTypeValues.map(requestStateType => (
                  <option value={requestStateType} key={requestStateType}>
                    {translate('clapApplicationApp.RequestStateType.' + requestStateType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('clapApplicationApp.privacyRequest.requestDate')}
                id="privacy-request-requestDate"
                name="requestDate"
                data-cy="requestDate"
                type="date"
              />
              <ValidatedField
                id="privacy-request-company"
                name="company"
                data-cy="company"
                label={translate('clapApplicationApp.privacyRequest.company')}
                type="select"
              >
                <option value="" key="0" />
                {companies
                  ? companies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="privacy-request-contentCreatto"
                name="contentCreatto"
                data-cy="contentCreatto"
                label={translate('clapApplicationApp.privacyRequest.contentCreatto')}
                type="select"
              >
                <option value="" key="0" />
                {contentCreators
                  ? contentCreators.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/privacy-request" replace color="info">
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

export default PrivacyRequestUpdate;
