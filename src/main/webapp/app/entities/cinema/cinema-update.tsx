import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICinema } from 'app/shared/model/cinema.model';
import { CinemaGenreType } from 'app/shared/model/enumerations/cinema-genre-type.model';
import { getEntity, updateEntity, createEntity, reset } from './cinema.reducer';

export const CinemaUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const cinemaEntity = useAppSelector(state => state.cinema.entity);
  const loading = useAppSelector(state => state.cinema.loading);
  const updating = useAppSelector(state => state.cinema.updating);
  const updateSuccess = useAppSelector(state => state.cinema.updateSuccess);
  const cinemaGenreTypeValues = Object.keys(CinemaGenreType);
  const handleClose = () => {
    props.history.push('/cinema');
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
      ...cinemaEntity,
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
          genres: 'ACTION',
          ...cinemaEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="clapApplicationApp.cinema.home.createOrEditLabel" data-cy="CinemaCreateUpdateHeading">
            <Translate contentKey="clapApplicationApp.cinema.home.createOrEditLabel">Create or edit a Cinema</Translate>
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
                  id="cinema-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('clapApplicationApp.cinema.genres')}
                id="cinema-genres"
                name="genres"
                data-cy="genres"
                type="select"
              >
                {cinemaGenreTypeValues.map(cinemaGenreType => (
                  <option value={cinemaGenreType} key={cinemaGenreType}>
                    {translate('clapApplicationApp.CinemaGenreType.' + cinemaGenreType)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cinema" replace color="info">
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

export default CinemaUpdate;
