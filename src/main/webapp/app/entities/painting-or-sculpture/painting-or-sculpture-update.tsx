import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPaintingOrSculpture } from 'app/shared/model/painting-or-sculpture.model';
import { getEntity, updateEntity, createEntity, reset } from './painting-or-sculpture.reducer';

export const PaintingOrSculptureUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const paintingOrSculptureEntity = useAppSelector(state => state.paintingOrSculpture.entity);
  const loading = useAppSelector(state => state.paintingOrSculpture.loading);
  const updating = useAppSelector(state => state.paintingOrSculpture.updating);
  const updateSuccess = useAppSelector(state => state.paintingOrSculpture.updateSuccess);
  const handleClose = () => {
    props.history.push('/painting-or-sculpture');
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
      ...paintingOrSculptureEntity,
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
          ...paintingOrSculptureEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="clapApplicationApp.paintingOrSculpture.home.createOrEditLabel" data-cy="PaintingOrSculptureCreateUpdateHeading">
            <Translate contentKey="clapApplicationApp.paintingOrSculpture.home.createOrEditLabel">
              Create or edit a PaintingOrSculpture
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
                  id="painting-or-sculpture-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('clapApplicationApp.paintingOrSculpture.materials')}
                id="painting-or-sculpture-materials"
                name="materials"
                data-cy="materials"
                type="text"
              />
              <ValidatedField
                label={translate('clapApplicationApp.paintingOrSculpture.techniques')}
                id="painting-or-sculpture-techniques"
                name="techniques"
                data-cy="techniques"
                type="text"
              />
              <ValidatedField
                label={translate('clapApplicationApp.paintingOrSculpture.size')}
                id="painting-or-sculpture-size"
                name="size"
                data-cy="size"
                type="text"
              />
              <ValidatedField
                label={translate('clapApplicationApp.paintingOrSculpture.place')}
                id="painting-or-sculpture-place"
                name="place"
                data-cy="place"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/painting-or-sculpture" replace color="info">
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

export default PaintingOrSculptureUpdate;
