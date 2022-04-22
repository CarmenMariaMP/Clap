import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPhotography } from 'app/shared/model/photography.model';
import { getEntity, updateEntity, createEntity, reset } from './photography.reducer';

export const PhotographyUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const photographyEntity = useAppSelector(state => state.photography.entity);
  const loading = useAppSelector(state => state.photography.loading);
  const updating = useAppSelector(state => state.photography.updating);
  const updateSuccess = useAppSelector(state => state.photography.updateSuccess);
  const handleClose = () => {
    props.history.push('/photography');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...photographyEntity,
      ...values,
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
          ...photographyEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="clapApplicationApp.photography.home.createOrEditLabel" data-cy="PhotographyCreateUpdateHeading">
            <Translate contentKey="clapApplicationApp.photography.home.createOrEditLabel">Create or edit a Photography</Translate>
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
                  id="photography-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('clapApplicationApp.photography.camera')}
                id="photography-camera"
                name="camera"
                data-cy="camera"
                type="text"
              />
              <ValidatedField
                label={translate('clapApplicationApp.photography.techniques')}
                id="photography-techniques"
                name="techniques"
                data-cy="techniques"
                type="text"
              />
              <ValidatedField
                label={translate('clapApplicationApp.photography.size')}
                id="photography-size"
                name="size"
                data-cy="size"
                type="text"
              />
              <ValidatedField
                label={translate('clapApplicationApp.photography.place')}
                id="photography-place"
                name="place"
                data-cy="place"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/photography" replace color="info">
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

export default PhotographyUpdate;
