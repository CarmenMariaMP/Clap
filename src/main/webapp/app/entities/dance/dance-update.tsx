import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDance } from 'app/shared/model/dance.model';
import { DanceGenreType } from 'app/shared/model/enumerations/dance-genre-type.model';
import { getEntity, updateEntity, createEntity, reset } from './dance.reducer';

export const DanceUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const danceEntity = useAppSelector(state => state.dance.entity);
  const loading = useAppSelector(state => state.dance.loading);
  const updating = useAppSelector(state => state.dance.updating);
  const updateSuccess = useAppSelector(state => state.dance.updateSuccess);
  const danceGenreTypeValues = Object.keys(DanceGenreType);
  const handleClose = () => {
    props.history.push('/dance');
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
      ...danceEntity,
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
          genres: 'FLAMENCO',
          ...danceEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="clapApplicationApp.dance.home.createOrEditLabel" data-cy="DanceCreateUpdateHeading">
            <Translate contentKey="clapApplicationApp.dance.home.createOrEditLabel">Create or edit a Dance</Translate>
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
                  id="dance-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('clapApplicationApp.dance.music')}
                id="dance-music"
                name="music"
                data-cy="music"
                type="text"
              />
              <ValidatedField
                label={translate('clapApplicationApp.dance.genres')}
                id="dance-genres"
                name="genres"
                data-cy="genres"
                type="select"
              >
                {danceGenreTypeValues.map(danceGenreType => (
                  <option value={danceGenreType} key={danceGenreType}>
                    {translate('clapApplicationApp.DanceGenreType.' + danceGenreType)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/dance" replace color="info">
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

export default DanceUpdate;
