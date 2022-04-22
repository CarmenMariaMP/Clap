import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dance.reducer';

export const DanceDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const danceEntity = useAppSelector(state => state.dance.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="danceDetailsHeading">
          <Translate contentKey="clapApplicationApp.dance.detail.title">Dance</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{danceEntity.id}</dd>
          <dt>
            <span id="music">
              <Translate contentKey="clapApplicationApp.dance.music">Music</Translate>
            </span>
          </dt>
          <dd>{danceEntity.music}</dd>
          <dt>
            <span id="genres">
              <Translate contentKey="clapApplicationApp.dance.genres">Genres</Translate>
            </span>
          </dt>
          <dd>{danceEntity.genres}</dd>
        </dl>
        <Button tag={Link} to="/dance" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dance/${danceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DanceDetail;
